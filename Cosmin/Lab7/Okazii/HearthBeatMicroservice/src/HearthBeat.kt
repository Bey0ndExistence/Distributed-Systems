import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import io.reactivex.rxjava3.kotlin.subscribeBy

class HearthBeatMicroservice{
    private var listenerSocket: ServerSocket
    private var reciveSubscribeObservable: Observable<String>
    private val subscribersConnections: MutableList<Socket> = mutableListOf()
    private val subscriptions = CompositeDisposable()

    companion object Constants {
        const val LISTENER_PORT = 1900
        const val AUCTIONEER_PORT = 1500
        const val AUCTIONEER_HOST = "localhost"
    }

    init {
        listenerSocket = ServerSocket(LISTENER_PORT)
        reciveSubscribeObservable = Observable.create<String> { emitter ->
            // se asteapta conexiuni din partea bidderilor
            while (true) {
                try {
                    val bidderConnection = listenerSocket.accept()
                    subscribersConnections.add(bidderConnection)

                    // se citeste mesajul de la bidder de pe socketul TCP
                    val bufferReader = BufferedReader(InputStreamReader(bidderConnection.inputStream))
                    val receivedMessage = bufferReader.readLine()

                    // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                    if (receivedMessage == null) {
                        // deci subscriber-ul respectiv a fost deconectat
                        bufferReader.close()
                        bidderConnection.close()

                        emitter.onError(Exception("Eroare: Bidder-ul ${bidderConnection.port} a fost deconectat."))
                    }

                    // se emite ce s-a citit ca si element in fluxul de mesaje
                    emitter.onNext(receivedMessage)
                } catch (e: SocketTimeoutException) {
                    emitter.onComplete()
                    break
                }
            }
        }
    }


    fun run() {
    }
}

fun main(args: Array<String>) {
    val bidderMicroservice = HearthBeatMicroservice()
    bidderMicroservice.run()
}