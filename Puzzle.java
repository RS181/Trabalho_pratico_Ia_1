
public class Puzzle {
    // Posicoes do espaco em branco na matriz
    // sendo a primeira posicao (0,0)
    private int x;
    private int y;
    // matriz que representa o jogo(futaramente mudar)
    //Tenho uma maneira de implementar com array (ver folhas)
    // mas para ja usar com matriz e mais tarde modificar
    //FAZENDO UM BACKUP DE Puzzle.java e Main.java com 
    //Implementacao usando matriz
    private int[][] tab;
    
    //estes valores so usamos para pesquisa Informada
    private int f_n;
    Puzzle() {

        tab = new int[4][4];
    }

    // Inicializa um puzzle com uma certa configuracao , apartir do vetor vec
    // e atribui as coordenadas ao espaco em branco (<=> ao 0 no nosso caso)
    public void set_state(int[] vec) {
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
            
                tab[i][j] = vec[k];
                //Atribuimos a coordenada do esplaco em branco
                if (tab[i][j] == 0){
                    x = i;
                    y = j;
                }
                k++;
            }
        }
    }

    // Imprime o estado atual de um dado tabuleiro
    @Override
    public String toString() {
        String ans = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ans += tab[i][j] + " ";
            }
            ans += "\n";
        }
        return ans;
    }

    // Funcao auxiliar que transforma tab -> vetor 1d
    public int[] to_vec() {
        int[] ans = new int[16];
        int pos = 0;
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[0].length; j++) {
                ans[pos] = tab[i][j];
                pos++;
            }
        }
        return ans;
    }
    /*
     * Funcao auxiliar que dado um array , um valor e um indice
     * conta o nr de inversoes que occorem de um dado valor (v[indice-1])
     * ate ao fim do array
     */

    public static int aux(int[] v, int valor, int indice) {
        int count = 0;
        for (int i = indice; i < v.length; i++) {

            if (valor > v[i] && v[i] != 0)
                count++;
        }
        return count;
    }

    /*
     * funcao que calcula o nr de inversoes segundo a seguinte definicao
     * Tendo um array , o par de telhas (nao necessariamente adjacentes) (a,b)
     * forma inversao sse a > b e a aparece antes d eb
     */
    public int nr_inversions(int[] v) {
        int count = 0;
        for (int i = 0; i < v.length; i++) {
            // System.out.println(v[i] + " tem " + aux(v, v[i], i+1) + " inversoes");
            count += aux(v, v[i], i + 1);
        }
        return count;
    }


    //! retorna uma string que indica a paridade da permutacao
    public String get_permutation_parity(){
        int Cx = get_x();
        int Cy = get_y();
        System.out.println("(" +Cx +","+Cy+")");
        String ans = "";
        //verica se esta numa posicao em verde
        if (((Cx == 0 || Cx ==2) && (Cy == 0 || Cy == 2)) ||    //linha 0 ou 2 e coluna 0 ou 2 
        ((Cx == 1 || Cx == 3) && (Cy == 1 || Cy == 3)))
        {
            ans = "even";
            return ans;
        }
        else
        {
            ans = "odd";
            return ans;
        } 
    }
    /*
     * 
     * R: Temos de confirmar  a paridade da permutacao
     *  e a posicao em branca  
     * 
     * Funcao que recbe um vetor 1d ,que representa o estado atual do puzzle,
     * e indica se o puzzle tem solucao ,segundo a seguinte definicao:
     * 
     * puzzle instance is solvable if (Tambem verifica se conseguimos atingir um estado
     * inicial ao estado final)
     * -> the blank is on an even row counting from the bottom (second-last,
     * fourth-last, etc.) and number of inversions is odd.
     * -> the blank is on an odd row counting from the bottom (last, third-last,
     * fifth-last, etc.) and number of inversions is even.
     */
    public boolean is_solvable() {
        int n = nr_inversions(to_vec());//numero de inversoes 
        // System.out.println("nr de inversoes =" + nr_inversions(to_vec()));
        // System.out.println("linha de x = " + x);
        // nr inversoes impar e espaco branco esta numa linha par contando de baixo para cima(<=> contar de cima para baixo)
        if ( (n % 2 != 0) && (x % 2  == 0))
            return true ;
        // nr inversoes par e espaco branco esta numa linha impar contando de baixo para cima(<=> contar de cima para baixo)
        else if ((n % 2 == 0 ) && (x % 2 != 0))
            return true;
        //caso contratrio nao tem solucao
        else 
            return false;
        
    }

    //move a telha a para a telha b e vice versa
    public void swap (int a1,int b1,int a2,int b2){
        int temp = tab[a1][b1];
        tab[a1][b1] = tab[a2][b2];
        tab[a2][b2] = temp;
    }

    //Valida se um dado movimento do espaco em branco e valida
    public boolean is_valid(){
        if ( x >= 4 || x < 0  || y >= 4 || y < 0 )
            return false;
        return true;
    }

    //copia o estado atual do puzzle e retorna-o
    public Puzzle copy(){
        Puzzle ans = new Puzzle();
        ans.x = x;
        ans.y = y;
        for (int i = 0 ; i < 4 ;i++){
            for (int j = 0 ; j < 4 ;j++){
                ans.tab[i][j] = tab[i][j];
            }
        }
        return ans ;
    }
    // getters
    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public int[][] get_state(){
        return tab;
    }

    //setters (Movimentos de pecas )

    //move peca branca para cima
    public boolean move_up(){
        x--;
        //se for valido guarda o movimento e faz as respetivas alteracoes
        if (is_valid()) {
            swap(x,y,x+1,y);
            return true;
        }else {//senao da uma mensagem de erro e reajusta o valor
            // System.out.println("Error : Out of bounds");
            x++;
            return false; 
        }  
    }

    //move a peca branca para baixo 
    public boolean move_down(){
        x++;
        //se for valido guarda o movimento e faz as respetivas alteracoes
        if (is_valid()){
            swap(x,y,x-1,y);
            return true;
        }else {//senao da uma mensagem de erro e reajusta o valor
            // System.out.println("Error : Out of bounds");
            x--; 
            return false;
        }
    }

    //move a peca branca para a esquerda
    public boolean move_left(){
        y--;
        //se for valido guarda o movimento e faz as respetivas alteracoes
        if (is_valid()){
            swap(x,y,x,y+1);
            return true;
        }else {//senao da uma mensagem de erro e reajusta o valor
            // System.out.println("Error : Out of bounds");
            y++; 
            return false;
        }
    }

    //move a peca branca para a direita
    public boolean move_right(){
        y++;
        //se for valido guarda o movimento e faz as respetivas alteracoes
        if (is_valid()){
            swap(x,y,x,y-1);
            return true;
        }else {//senao da uma mensagem de erro e reajusta o valor
            // System.out.println("Error : Out of bounds");
            y--; 
            return false;
        }
    }

    public int get_f_n(){
        return f_n;
    }

    //inicializa a variavel que representa a funcao 
    //heuristica f(n) = h(n) + g(n)
    //para o metodo A*
    public void set_f_n(int h_n,int g_n){
        f_n = h_n + g_n;
    }

    //inicializa a variavel que representa a funcao 
    //heuristica f(n) = h(n) 
    //para o metodo Gulosa com heuristica
    public void set_f_n(int h_n){
        f_n = h_n;
    }



}
