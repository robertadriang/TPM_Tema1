public class Pregatar{
    final Oala oala;
    int id;

    int nr_salbatici;
    int[] label; // Prioritatea unui thread asupra bucatarului (conf. alg. Bakery)
    boolean[] flag; // Flag-ul unui thread asupra bucatarului (conf. alg. Bakery)

    public Pregatar(Oala oala, int id,int nr_salbatici) {
        this.oala = oala;
        this.id = id;
        this.nr_salbatici = nr_salbatici + 1;
        this.label = new int[this.nr_salbatici];
        this.flag = new boolean[this.nr_salbatici];
    }

    public void bagaMancare(int id_salbatic, int label_salbatic_care_cheama) {
        System.out.println("Scarlatescu a fost chemat de "+id_salbatic+" sa umple oala!");
        oala.chemat[id_salbatic-1]++;
        oala.lock(id); // Blocheaza accesul asupra oalei
        if(oala.getCurent()==0){ // Bucatarul paot eumple doar daca oala este complet goala
            oala.reumple();
        }
        else{
            System.out.println("Scarlatescu vede ca nu-i goala oala");
        }
        oala.label[id_salbatic]=label_salbatic_care_cheama; // Salbaticul care cheama bucatarul trebuie sa isi pastreze prioritatea
        oala.flag[id_salbatic]=true;
        oala.unlock(id); // Deblocheaza accesul asupra oalei

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
        flag[id] = true;
        int maxim = 0;
        for (int j = 0; j < this.nr_salbatici; ++j) {
            if (maxim < label[j]) {
                maxim = label[j];
            }
        }
        label[id] = maxim + 1;
        ///while (exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)) {};
        while (bakeryCondition(id)) {}
    }

    // Unlock Bakery
    void unlock(int id){
        flag[id]=false;
    }
}
