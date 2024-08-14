import org.apache.commons.net.ftp.FTPSClient
import java.util.Properties
import java.util.concurrent.TimeUnit


data class FTPSAttemptResult(val username: String, val password: String?, val responseTimes: List<Long>)
{
    fun averageResponseTime() : Double {
        return responseTimes.average()
    }
}

fun main(args: Array<String>) {
    val hostname = "your-ftps-hostname-here"
    val port = 21
    val credentials = mapOf(
        "username1" to "password1",
        "username2" to "0123456789".repeat(50),
        "username3" to "0123456789".repeat(50)
    )

    var results = mutableListOf<FTPSAttemptResult>()

    for (credentialPair in credentials) {
        val usernameResponseTimes = List(10) {
            tryLogin(hostname, port, credentialPair.key)
        }
        results.add(FTPSAttemptResult(credentialPair.key,null,usernameResponseTimes))

        val passwordResponseTimes = List(5) {
            tryLogin(hostname, port, credentialPair.key,credentialPair.value)
        }
        results.add(FTPSAttemptResult(credentialPair.key,credentialPair.value,passwordResponseTimes))
    }

    println("Username\tPassword\tAvg. Response Time\tResponse Time Test Case Results")
    for(result in results) {
        println("${result.username}\t${if(result.password!=null) {result.password} else {"null"}}\t${result.averageResponseTime()}\t${result.responseTimes.joinToString("\t")}")
    }
}

fun tryLogin(hostname : String, port : Int, username : String, password : String? = null) : Long {
    val ftpsClient = FTPSClient()
    ftpsClient.connect(hostname,port)
    val nonNullPassword = password ?: ""

    val beforeAttempt = System.nanoTime()
    try {
        println("Attempting to connect: $username@$hostname:$port ${password ?: "null"}")
        ftpsClient.login(username, nonNullPassword)
        println("Login Success")
    }
    catch (e: Exception)
    {
        e.printStackTrace()
    }
    val afterAttempt = System.nanoTime()
    val attemptResponseTime = TimeUnit.NANOSECONDS.toMillis(afterAttempt - beforeAttempt)
    if(ftpsClient.isConnected)
        ftpsClient.disconnect()
    println("Response Time: $attemptResponseTime")
    return attemptResponseTime
}