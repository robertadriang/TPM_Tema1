//#3. Consideram urmatoarea problema: Un trib de salbatici mananca dintr-o singura oala mare ce are o capacitate de N portii.
// Cand un membru al tribului mananca, va lua o portie din oala daca oala are cel putin o portie disponibila.
// Daca oala este goala, membrul de trib va ordona bucatarului sa reumple oala si va astepta pana ce aceasta este din nou complet plina.
// Bucatarul face exclusiv reumpleri complete (N portii).
// Pe scurt: membrii tribului nu pot lua o portie din oala daca aceasta este goala si bucatarul nu poate reumple oala decat daca este goala.
//   a) (5 puncte) Scrieti un program care sa simuleze comportamentul membrilor de trib si al bucatarului, unde fiecare dintre acestia este reprezentat de un thread,
//   iar oala este o resursa partajata, respectand constrangerile enuntate mai sus.
//   Considerati ca fiecare dintre membrii de trib doreste sa manance doar o singura masa,
//   dar numarul lor total este mai mare decat capacitatea oalei,
//   deci aceasta va necesita reumpleri.
//
//   b) (5 puncte) Considerati situatia in care membrii de trib sunt permanent flamanzi
//   (thread-urile executa o bucla continua incercand sa ia o noua portie din oala dupa ce mananca o data).
//   Modificati programul intr-un mod in care se asigura garantat ca fiecare dintre membrii de trib va manca la un moment dat
//   (hint: ganditi-va la o modalitate de a face executia fair, astfel incat un membru sa nu manance mai des decat altul).
//   Numarul de membri de trib este fix, si fiecare dintre acestia il cunoaste. Masurati timpul de executie si comparati-l cu cel de la punctul a).
//   Raportati rezultatele obtinute intr-un fisier.
//   Restrictii:
//   Nu este permisa utilizarea in implementarea solutiei a tipurilor atomice din Java.
//   Nu este permisa utilizarea in implementarea solutiei pentru punctul b) a mecanismelor din Java care ofera implicit garantii de fairness (ex., utilizarea unei instante Semaphore cu suport de fairness ce se poate initializa prin constructor).
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int NUMAR_THREADS = 3;

        int []nr_mancat=new int[NUMAR_THREADS];

        Oala oala=new Oala(1,NUMAR_THREADS,nr_mancat);

        var scarlatescu=new Pregatar(oala,0,NUMAR_THREADS);

        Thread[] salbatici = new Thread[NUMAR_THREADS];
        for(int i = 0; i < NUMAR_THREADS; i++){
            salbatici[i] = new Salbatic(oala, scarlatescu, i + 1, nr_mancat);
        }

        for(int i = 0; i < NUMAR_THREADS; i++){
            salbatici[i].start();
        }
        for(int i = 0; i < NUMAR_THREADS; i++){
            salbatici[i].join();
        }

    }
}
