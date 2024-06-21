import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import kotlin.Exception
import kotlin.random.Random
import kotlin.system.exitProcess

class BidderMicroservice {
    private var auctioneerSocket: Socket
    private var auctionResultObservable: Observable<String>
    private var myIdentity: String = "[BIDDER_NECONECTAT]"

    companion object Constants {
        const val AUCTIONEER_HOST = "localhost"
        const val AUCTIONEER_PORT = 1500
        const val MAX_BID = 10_000
        const val MIN_BID = 1_000
        val names = listOf(
                    "Edith Arispe",
                    "Barbra Bernat",
                    "Ashley Ricco",
                    "Alan Kluth",
                    "Brittny Shirkey",
                    "Aimee Ashcraft",
                    "Aleen Hatter",
                    "Santina Wolfgang",
                    "Kyung Reys",
                    "Claris Regnier",
                    "Cordell Geary",
                    "Pamala Yelle",
                    "Dung Garlow",
                    "Kari Norsworthy",
                    "Dalton Reale",
                    "Annemarie Wakeman",
                    "Adelle Depaul",
                    "Sherry Kiley",
                    "Shaquita Stemple",
                    "Benita Hair",
                    "Faye Brevard",
                    "Merry Calcote",
                    "Marylyn Woodworth",
                    "Tommye Zeledon",
                    "Barry Mulligan",
                    "Jacinda Perlmutter",
                    "Miesha Lusby",
                    "Janita Wild",
                    "Cruz Strahan",
                    "Garth Konrad",
                    "Malinda Engelhard",
                    "Araceli Fulop",
                    "Rita Anguiano",
                    "Carrie Tell",
                    "Augustina Hass",
                    "Lawana Wongus",
                    "Tillie Segarra",
                    "Ara Samet",
                    "Theola Garmon",
                    "Dino Parras",
                    "Lemuel Dorazio",
                    "Johnsie Race",
                    "Sydney Dam",
                    "Anastasia Tercero",
                    "Dortha Ellenburg",
                    "Deneen Notter",
                    "Arla Alban",
                    "Brande Looper",
                    "Jolynn Marlin",
                    "Suzann Chiaramonte")

        val mailServices = listOf("gmail.com", "yahoo.com", "mail.com", "aol.com", "student.tuasi.ro")
    }

    init {
        try {
            auctioneerSocket = Socket(AUCTIONEER_HOST, AUCTIONEER_PORT)
            println("M-am conectat la Auctioneer!")

            myIdentity = "[${auctioneerSocket.localPort}]"

            // se creeaza un obiect Observable ce va emite mesaje primite printr-un TCP
            // fiecare mesaj primit reprezinta un element al fluxului de date reactiv
            auctionResultObservable = Observable.create<String> { emitter ->
                // se citeste raspunsul de pe socketul TCP
                val bufferReader = BufferedReader(InputStreamReader(auctioneerSocket.inputStream))
                val receivedMessage = bufferReader.readLine()

                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                if (receivedMessage == null) {
                    bufferReader.close()
                    auctioneerSocket.close()

                    emitter.onError(Exception("AuctioneerMicroservice s-a deconectat."))
                    return@create
                }

                // mesajul primit este emis in flux
                emitter.onNext(receivedMessage)

                // deoarece se asteapta un singur mesaj, in continuare se emite semnalul de incheiere al fluxului
                emitter.onComplete()

                bufferReader.close()
                auctioneerSocket.close()
            }
        } catch (e: Exception) {
            println("$myIdentity Nu ma pot conecta la Auctioneer!")
            exitProcess(1)
        }
    }

    private fun bid() {
        // se genereaza o oferta aleatorie din partea bidderului curent
        val pret = Random.nextInt(MIN_BID, MAX_BID)

        val name = names[Random.nextInt(0, names.size - 1)]
        val email = name.replace(' ', '.').toLowerCase() + "@" + mailServices[Random.nextInt(0, mailServices.size)]

        // se creeaza mesajul care incapsuleaza oferta
        val biddingMessage = Message.create("${auctioneerSocket.localAddress}:${auctioneerSocket.localPort}",
            "licitez $pret", name, "074" + Random.nextInt(1_000_000, 9_999_999), email)

        // bidder-ul trimite pretul pentru care doreste sa liciteze
        val serializedMessage = biddingMessage.serialize()
        auctioneerSocket.getOutputStream().write(serializedMessage)

        // exista o sansa din 2 ca bidder-ul sa-si trimita oferta de 2 ori, eronat
        if (Random.nextBoolean()) {
            auctioneerSocket.getOutputStream().write(serializedMessage)
        }
    }

    private fun waitForResult() {
        println("$myIdentity Astept rezultatul licitatiei...")
        // bidder-ul se inscrie pentru primirea unui raspuns la oferta trimisa de acesta
        val auctionResultSubscription = auctionResultObservable.subscribeBy(
            // cand se primeste un mesaj in flux, inseamna ca a sosit rezultatul licitatiei
            onNext = {
                val resultMessage: Message = Message.deserialize(it.toByteArray())
                println("$myIdentity Rezultat licitatie: ${resultMessage.body}")
            },
            onError = {
                println("$myIdentity Eroare: $it")
            }
        )

        // se elibereaza memoria obiectului Subscription
        auctionResultSubscription.dispose()
    }

    fun run() {
        bid()
        waitForResult()
    }
}

fun main(args: Array<String>) {
    val bidderMicroservice = BidderMicroservice()
    bidderMicroservice.run()
}