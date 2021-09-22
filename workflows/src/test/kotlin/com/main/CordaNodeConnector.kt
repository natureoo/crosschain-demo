package com.main

import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCConnection
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.node.NodeInfo
import net.corda.core.utilities.NetworkHostAndPort

/**
 * @author nature
 * @date 22/9/21 上午10:21
 * @email 924943578@qq.com
 */
class CordaNodeConnector(
        val host: String,
        val port: Int,
        val userName: String,
        val password: String
) {

    private lateinit var client: CordaRPCClient
    private lateinit var conn: CordaRPCConnection
    lateinit var proxy: CordaRPCOps

    fun connect() {
        val rpcAddress = NetworkHostAndPort(host, port)
        client = CordaRPCClient(rpcAddress)
        conn = client.start(userName, password)
        proxy = conn.proxy
        println("Successfully connect to R3 Corda, host: ${host}, port: ${port}, details: ${this.getNodeInfo()}")
    }

    fun destroy() {
        conn.notifyServerAndClose()
    }

    fun getNodeInfo(): List<NodeInfo?>? {
        return if (proxy != null) {
            proxy!!.networkMapSnapshot()
        } else {
            emptyList()
        }
    }
}