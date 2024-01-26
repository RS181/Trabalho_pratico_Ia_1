import java.util.*;
//No que contem o caminho e o custo associado de um puzzle
class Node {
    Puzzle current;//state
    ArrayList <Puzzle> path;
    int depth; //profundidade do no 
    int cost; //numero de nos,sem contar o inicial , que foram necessarios para chegar a este no
    Puzzle parent;//no pai que gerou o no filho (elemento anterior no path)

    //Construtor
    Node(Puzzle pa ,ArrayList<Puzzle> p,int c){
        current = pa;
        path = p;
        depth = c;
        if (path.size() > 1){
            //associamos o no pai que o gerou
            parent = path.get(path.size()-1);
        }
        cost = path.size()-1;
    }
    
    Node(){
        path = new ArrayList<>();
    }

    //!criamos um metodo equals customizado , que compara
    //! dois nos atraves do puzzle current de cada

    @Override
    public boolean equals(Object o){

        if (o == this)
            return true;
        if (!(o instanceof Node))
            return false;

        Node n = (Node) o;

        return current.toString().compareTo(n.toString()) == 0;
    }



    // Imprime o estado atual de um dado tabuleiro
    @Override
    public String toString() {
        String ans = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ans += current.get_state()[i][j] + " ";
            }
            ans += "\n";
        }
        return ans;
    }


}
