import java.io.PrintWriter;
import java.util.Arrays;

public class Oala {
    int maxim; // Capacitate maxima oala
    int curent; // Portii ramase in oala
    int nr_salbatici;
    int[] label; // Prioritatea unui thread asupra oalei (conf. alg. Bakery)
    boolean[] flag; // Flag-ul unui thread asupra oalei (conf. alg. Bakery)
    static int[] nr_mancat; // Nr de portii mancate de fiecare salbatic
    int[] chemat;  // De cate ori este chemat bucatarul de fiecare salbatic
    static public long startTime = 0;
    PrintWriter out;
    public Oala(int maxim, int nr_salbatici, int[] nr_mancat, PrintWriter out) {
        System.out.println("S-a initializat oala!");
        this.maxim = maxim;
        this.curent = maxim;
        this.nr_salbatici = nr_salbatici + 1; /// Salbatici+Bucatar
        this.label = new int[this.nr_salbatici];
        this.flag = new boolean[this.nr_salbatici];
        Oala.nr_mancat = nr_mancat;
        this.chemat = new int[this.nr_salbatici];
        this.out = out;
    }

    public int getCurent() {
        return curent;
    }

    void mananca(int id) {
        curent--;
        System.out.println(id+" a mancat din oala. Ramas:"+curent);
        System.out.println("Label dupa mancat[] " + Arrays.toString(label));
        System.out.println("Acces la oala cerut[] " + Arrays.toString(flag));
        // Verifica daca fiecare a mancat cel putin o data
        for(int i = 0; i < nr_salbatici - 1; ++i){
            if(nr_mancat[i] == 0)
                return;
        }
        long duration = (System.nanoTime() - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        out.println(duration);
        out.flush();
        System.exit(1);
    }

    void reumple() {
        curent = maxim;
//        System.out.println("S-a umplut oala!");
//        System.out.println("Label[] " + Arrays.toString(label));
        System.out.println("Nr mancat[] "+Arrays.toString(nr_mancat));
//        System.out.println("Nr chemat[] "+Arrays.toString(this.chemat));

    }

    // Compara doua tuple (conf. alg Bakery)
    boolean isFirstBigger(int i, int j) {
        /// (a,b)>(c,d) IFF a>c OR a=c and b>d
        return label[i] > label[j] || (label[i] == label[j] && i > j);
    }

    // Verifica daca threadul trebuie sa astepte (conf. alg. Bakery).
    boolean bakeryCondition(int i) {
        //exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)
        for (int k = 0; k < this.nr_salbatici; ++k) {
            if (k != i && flag[k] && isFirstBigger(i, k))
                return true;
        }
        return false;
    }

    // Lock Bakery
    void lock(int id) {
        // Bucatarul trebuie sa aiba mereu prioritate maxima. Pt ca bucatarul este primul element din array ii vom pune o prioritate negativa,
        // astfel asigurandu-ne ca mereu va fi primul care va primi acces la oala
        if (id == 0) {
            flag[id] = true;
            label[id] = -1;
            return;
        }
        //System.out.println("Salbaticul "+id+" a blocat oala");
        flag[id] = true;
        int maxim = 0;
        for (int j = 0; j < this.nr_salbatici; ++j) {
            if (maxim < label[j]) {
                maxim = label[j];
            }
        }
        label[id] = maxim + 1;
        //while (exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)) {};
        while (bakeryCondition(id)) {
        }
    }

    void lock_without_label(int id) {
        flag[id] = true;
        ///while (exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)) {};
        while (bakeryCondition(id)) {}
    }

    // Unlock Bakery
    void unlock(int id) {
        flag[id] = false;
    }
}
