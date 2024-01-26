
import java.util.*;

class Main {

    /*
     * Retorna se e possivel chegar de um estado
     * inicial a um estado final
     */
    public static boolean Check_Initial_to_Final(Puzzle p1, Puzzle p2) {
        if (!(p1.is_solvable() ^ p2.is_solvable())
                && p1.get_permutation_parity().equals(p2.get_permutation_parity()))
            return true;
        else
            return false;
    }

    /*
     !Nao tem fim (pesquisa infinitamente em profundidade)
     */
    public static Node DFS(Node startNode, Puzzle Final) {
        // ?Pilha "principal"

        Stack<Node> principal = new Stack<>();
        principal.push(startNode);

        // ?Pilha que guarda os Puzzle ja visitados

        Stack<Node> visited = new Stack<>();

        // ? pilha que diz o numero de filhos de cada no

        Stack<Integer> sons = new Stack<>();

        while (!(principal.isEmpty())) {
            Node n = principal.peek();
            if (isGoal(n, Final)){
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);

                return n;
            }
            
            if (!(sons.isEmpty())){
                Integer aux = sons.pop();
                aux--;
                if (aux > 0)
                    sons.push(aux);
                //!libertamos o espaco em memoria 
                if (aux == 0){
                    principal.pop();
                    visited.pop();
                }
            }

            //! so faz a procura deste no e descendentes se o mesmo nao estiver no visited
            if (!(visited.contains(n))){
                //?Geramos os descendentes do n
                ArrayList<Node> desc = MakeDescendants(n);
                generated_nodes+=desc.size();

                //!adiciona o numero de filhos do n ao pilha sons (se nao for no folha)
                if (desc.size() > 0 )
                    sons.push(desc.size());

                for (int i = desc.size() -1 ;i >= 0 ;i--){
                    //! criamos um novo no filho nao esquecendo de
                    //! adicionar ao caminho feito ate agora esse novo no filho
                    //! atualizando o custo , path e current(puzzle atual)
                    Puzzle next = desc.get(i).current;
                    ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                    newPath.add(next);
                    Node filho = new Node(next, newPath, n.depth + 1);
                    if (!(visited.contains(filho))){
                        visited.push(filho);
                        principal.push(filho);
                    }
                }
                 
            }

        }
        return null;
    }

    // Funcao auxiliar da BFS
    public static void BFS_helper(ArrayList<Node> descendant, Queue<Node> q, Queue<Node> qf) {
        // adiciona os nos a Queue q da esquerda para a direita
        for (int i = 0; i < descendant.size(); i++)
            qf.add(descendant.get(i));
        while (!(qf.isEmpty()))
            q.add(qf.remove());

    }

    // Bfs
    public static Node BFS(Node startNode, Puzzle Final) {
        Queue<Node> q = new LinkedList<>();
        q.add(startNode);
        while (q.isEmpty() == false) {
            Node n = new Node();
            n = q.remove();
            if (isGoal(n, Final)) {
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);

                return n;
            }
            ArrayList<Node> descendantList = new ArrayList<>();

            descendantList = MakeDescendants(n);

            // Acresentamos variavel que guarda o numero de nos gerados/armazenados
            generated_nodes += descendantList.size();

            // Pesquisa em largura (Parece bem mas pedir a professora para dar uma vista de
            // olhos na folha a explicar o que faz)
            Queue<Node> qf = new LinkedList<>();
            BFS_helper(descendantList, q, qf);
        }
        System.out.println("Cheguei ao fim");
        return null;
    }

    /*
     * Funcao auxiliar da DFS iterativa
     * Se nao encontrar solucao com profundidade maxima k
     * entao aumenta k (k = k+1) e continua e faz novamente a pesquisa com o
     * novo limite maximo.
     * max_depth e definido por o utilizador
     */
    public static void DFS_Iterative_helper(Node startNode, Puzzle Final, int max_depth) {
        for (int i = 0; i <= max_depth; i++) {
            Node ans = new Node();
            ans = DFS_Iterative(startNode, Final, i);
            if (ans != null) {
                if (isGoal(ans, Final)) {
                    System.out.println("Operadores : " + Actions(ans.path));
                    System.out.println("numero de passos = " + ans.cost);
                    System.out.println("Encontrei solucao no nivel de profundidade " + ans.depth);
                    System.out.println("Caminho da solucao  = \n" + ans.path);
                    System.out
                            .println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                                    + generated_nodes);
                    return;
                }
            }
            generated_nodes = 1;// faz reset ao numero de nos armazenados (porque vamos reniciar a pesquisa
                                // desde do inicio)
        }
        System.out.println("Com o limite de profundidade " + max_depth + " nao encontrou solucao");
    }

    /*
     * Procura iterativa DFS que procura uma solucao ate uma certa profundidade
     */
    public static Node DFS_Iterative(Node startNode, Puzzle Final, int max_depth) {
        // COnjunto que guarda os nos que ja foram visitados
        Set<Puzzle> visited = new HashSet<>();
        // Pilha que vai aramezenar os nos a serem visitados
        Stack<Node> s = new Stack<>();
        // adionamos o no de inicio (raiz na arvore de procura)
        s.push(startNode);
        visited.add(startNode.current);

        while (!(s.isEmpty())) {
            Node n = s.pop();
            // se o no atual for o estado final ou objetivo retornamos o no
            if (isGoal(n, Final))
                return n;

            /*
             * se a profundidade do no atual <= max_depth
             * continuamos a gerar os seus filhos e a fazer a procura em
             * profundidade
             */
            if (n.depth <= max_depth) {
                // lista dos descendentes do no n
                ArrayList<Node> desc = MakeDescendants(n);
                generated_nodes += desc.size();
                /*
                 * Explicacao do ciclo abaixo:
                 * A cada iteracao fazemos o seguinte:
                 * 1)percorremos os descendentes ou sucessores do no n (esquerda para a direita)
                 * por causa da insercao da stack
                 * 2)Se o puzzle associado a esse no nao tiver sido ainda visatado,adicionamo-o
                 * a stack e a vsited
                 * Sendo assim possivel fazer a pesquisa em profundidade iterativa
                 */
                for (int i = desc.size() - 1; i >= 0; i--) {
                    // criamos um novo no filho nao esquecendo de
                    // adicionar ao caminho feito ate agora esse novo no filho
                    // atualizando o custo , path e current(puzzle atual)
                    Puzzle next = desc.get(i).current;
                    ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                    newPath.add(next);
                    Node filho = new Node(next, newPath, n.depth + 1);
                    // verifica se o puzzle associado ao no filho ja foi visitado
                    // se nao: adicionamos a visited o puzzle associado ao filho
                    // e adicionamos a stack o Node filho
                    if (!visited.contains(next)) {
                        visited.add(next);
                        s.push(filho);
                    }
                }
            }
        }
        // nao encontrou solucao
        return null;

    }

    /*
     * devolve o valor da funcao heuristica
     * que nos da o numero de telhas fora do
     * sitio (excluindo a telha em branco)
     */
    public static int Hamming_Distance(Node n, Puzzle Final) {
        int count = 0;
        // Tabuleiro do puzzle atual e do puzzle final
        int[][] cur = n.current.get_state();
        int[][] f = Final.get_state();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cur[i][j] != f[i][j] && cur[i][j] != 0)
                    count++;
            }
        }
        return count;
    }

    // !funcao que retorna as coordenadas do puzzle final onde esta o elemento com
    // valor a
    // ?funcao auxiliar de Manhatan distance
    public static int[] find_position(int a, int[][] Final) {
        // contem a posicao ,linha k e coluna p , do valor a na configuracao fina ou
        // objetivo do puzzle
        int[] ans = new int[2];
        for (int k = 0; k < 4; k++) {
            for (int p = 0; p < 4; p++) {
                if (Final[k][p] == a) {
                    // Encontrei a posicao no puzle final onde esta o elemento
                    ans[0] = k;
                    ans[1] = p;
                    return ans;
                }
            }
        }
        return null;// caso nao encontra retorna null, para indicar que houve erro
    }

    /*
     * devolve o valor da funcao heuristica que nos da
     * o somatório das distâncias de cada peça ao seu lugar na
     * configuração final
     * 
     * x = linha i x' = linha k
     * y = coluna j y' = coluna p
     * 
     * Manhattan_distance {(x,y),(x',y')} = Abs(x-x') + Abs(y-y')
     */
    public static int Manhantan_distance_helper(int i, int j, int k, int p, int[][] cur, int[][] Final) {
        // System.out.println("Distancia da telha " + cur[i][j] + " ate ao seu destino e
        // = " + (Math.abs(i-k) + Math.abs(j-p)));
        return Math.abs(i - k) + Math.abs(j - p);

    }

    public static int Manhattan_distance(Node n, Puzzle Final) {
        // estado atual do tabuleiro do puzzle
        int[][] cur = n.current.get_state();
        // esatdo final ou objetivo do tabuleiro do puzzle
        int[][] P_Final = Final.get_state();
        // Variavel que vai guardar o somatório das distâncias de cada peça ao seu lugar
        // na
        // configuração final
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                /*
                 * array que contem a posicao no tabuleiro do puzzle final
                 * do elemento cur[i][j] do tabuleiro do Puzzle "original"
                 * 
                 */
                int[] aux = find_position(cur[i][j], P_Final);
                count += Manhantan_distance_helper(i, j, aux[0], aux[1], cur, P_Final);
            }
        }
        // System.out.println("somatorio das distancias de cada peca ao seu lugar na
        // configuraçao final = " + count);

        return count;
    }
    /*
     * Nota:
     * Podiamos ter juntado as duas variantes de metodo de procura A* ,porque a
     * unica coisa que difere
     * e o calculo de h(n) , respetiva heuristica.
     * 
     * f(n) = g(n) + h(n)
     * h(n) : "Somatorio das pecas fora do lugar"
     * g(n) : "Custo do caminho , ate ao momento, para chegar ao no n" (no meu caso
     * usei a profundidade)
     * 
     * ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ TER ATENCAO A CORRECAO DA MONOTONIA ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️
     * NAO APLICA A CORRECAO DE MONOTONIA
     * VER CADERNO AULA 3
     */

    public static Node A_Star_Humming(Node starNode, Puzzle Final) {
        // criamos um comparador customizado para Node
        // que compara pelo custo de cada no
        // (Testar mais o comparador)
        Comparator<Node> nodeCostComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return n1.current.get_f_n() - n2.current.get_f_n();
            }
        };
        Queue<Node> Open = new LinkedList<Node>();
        Open.add(starNode);
        // PriorityQueue que guarda por ordem crescente do custo de f(n) dos nos
        // (Verificar se funciona!!)
        PriorityQueue<Node> Closed = new PriorityQueue<>(nodeCostComparator);
        while (!(Open.isEmpty())) {
            Node n = Open.remove();
            if (isGoal(n, Final)) {
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);
                return n;
            }
            // gera os descendentes do no n
            ArrayList<Node> desc = MakeDescendants(n);

            // Acresentamos variavel que guarda o numero de nos gerados/armazenados
            generated_nodes += desc.size();

            // atribui o respetivo custo a cada um desses descendentes e inserir-los
            // na Priority Queue
            for (int i = 0; i < desc.size(); i++) {
                // Calculamos e atribuimos o respetivo valor de
                // f(n) ao descdentes
                Node aux = desc.get(i);
                int h_n = Hamming_Distance(aux, Final);
                int g_n = aux.cost;
                desc.get(i).current.set_f_n(h_n, g_n);
                // criamos um novo no filho nao esquecendo de
                // adicionar ao caminho feito ate agora esse novo no filho
                // atualizando o custo , path e current(puzzle atual)
                ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                Puzzle next = desc.get(i).current;
                newPath.add(next);
                Node filho = new Node(next, newPath, n.depth + 1);
                // Adicionamos a PriorityQueue
                // que vai ordena-los por ordem crescente de f(n)
                Closed.add(filho);
            }

            // System.out.println();
            // Adicionamos o elemento de menor custo a Queue Open
            // System.out.println(Closed);
            Open.add(Closed.poll());
            // System.out.println(Open);
        }
        // nao encontrou resposta
        return null;
    }

    /*
     * Nota:
     * Podiamos ter juntado as duas variantes de metodo de procura A* ,porque a
     * unica coisa que difere
     * e o calculo de h(n) , respetiva heuristica.
     * 
     * f(n) = g(n) + h(n)
     * h(n) :
     * "somatório das distâncias de cada peça ao seu lugar na configuração final"
     * g(n) : "Custo do caminho , ate ao momento, para chegar ao no n" (no meu caso
     * usei a profundidade)
     * 
     * ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ TER ATENCAO A CORRECAO DA MONOTONIA ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️
     * NAO APLICA A CORRECAO DE MONOTONIA
     * VER CADERNO AULA 3
     */

    public static Node A_Star_Manhattan(Node starNode, Puzzle Final) {
        // criamos um comparador customizado para Node
        // que compara pelo custo de cada no
        // (Testar mais o comparador)
        Comparator<Node> nodeCostComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return n1.current.get_f_n() - n2.current.get_f_n();
            }
        };
        Queue<Node> Open = new LinkedList<Node>();
        Open.add(starNode);
        // PriorityQueue que guarda por ordem crescente do custo de f(n) dos nos
        // (Verificar se funciona!!)
        PriorityQueue<Node> Closed = new PriorityQueue<>(nodeCostComparator);
        while (!(Open.isEmpty())) {
            Node n = Open.remove();
            if (isGoal(n, Final)) {
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);
                return n;
            }
            // gera os descendentes do no n
            ArrayList<Node> desc = MakeDescendants(n);
            // Acresentamos variavel que guarda o numero de nos gerados/armazenados
            generated_nodes += desc.size();

            // atribui o respetivo custo a cada um desses descendentes e inserir-los
            // na Priority Queue
            for (int i = 0; i < desc.size(); i++) {
                // Calculamos e atribuimos o respetivo valor de
                // f(n) ao descdentes
                Node suc = desc.get(i);
                int h_n = Manhattan_distance(suc, Final);
                int g_n = suc.cost;
                desc.get(i).current.set_f_n(h_n, g_n);
                // criamos um novo no filho nao esquecendo de
                // adicionar ao caminho feito ate agora esse novo no filho
                // atualizando o custo , path e current(puzzle atual)
                ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                Puzzle next = desc.get(i).current;
                newPath.add(next);
                Node filho = new Node(next, newPath, n.depth + 1);
                // Adicionamos a PriorityQueue
                // que vai ordena-los por ordem crescente de f(n)
                Closed.add(filho);
            }
            // System.out.println();
            // Adicionamos o elemento de menor custo a Queue Open
            // System.out.println(Closed);
            Open.add(Closed.poll());
            // System.out.println(Open);
        }
        // nao encontrou resposta
        return null;

    }

    /*
     * f(n) = h(n)
     * 
     * h(n) = "Somatorio das pecas fora do lugar"
     * 
     * ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️
     * (CONFIRMAR INFORMACAO ABAIXO)
     * Igual a A_Star_Humming, unica difenca e que so adicionamos h(n) a f(n)
     * 
     * NAO APLICA A CORRECAO DE MONOTONIA
     * ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️ ❗️❗️❗️❗️❗️❗️❗️❗️❗️❗️
     */
    public static Node Greedy_With_Heuristic_Hamming(Node starNode, Puzzle Final) {
        // criamos um comparador customizado para Node
        // que compara pelo custo de cada no
        // (Testar mais o comparador)
        Comparator<Node> nodeCostComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return n1.current.get_f_n() - n2.current.get_f_n();
            }
        };
        Queue<Node> Open = new LinkedList<Node>();
        Open.add(starNode);
        // PriorityQueue que guarda por ordem crescente do custo de f(n) dos nos
        // (Verificar se funciona!!)
        PriorityQueue<Node> Closed = new PriorityQueue<>(nodeCostComparator);

        Set<Puzzle> visited = new HashSet<>();
        visited.add(starNode.current);
        while (!(Open.isEmpty())) {
            Node n = Open.remove();
            if (isGoal(n, Final)) {
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);
                return n;
            }
            // gera os descendentes do no n
            ArrayList<Node> desc = MakeDescendants(n);

            // Acresentamos variavel que guarda o numero de nos gerados/armazenados
            generated_nodes += desc.size();

            // atribui o respetivo custo a cada um desses descendentes e inserir-los
            // na Priority Queue
            for (int i = 0; i < desc.size(); i++) {
                // Calculamos e atribuimos o respetivo valor de
                // f(n) ao descendentes
                Node aux = desc.get(i);
                int h_n = Hamming_Distance(aux, Final);
                desc.get(i).current.set_f_n(h_n);
                // criamos um novo no filho nao esquecendo de
                // adicionar ao caminho feito ate agora esse novo no filho
                // atualizando o custo , path e current(puzzle atual)
                ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                Puzzle next = desc.get(i).current;
                newPath.add(next);
                Node filho = new Node(next, newPath, n.depth + 1);
                // Adiciona a priorityQueue caso nao tenha
                // Adicionamos a PriorityQueue
                // que vai ordena-los por ordem crescente de f(n)
                if (!visited.contains(next)) {
                    Closed.add(filho);
                    visited.add(next);
                }
                // System.out.println();
                // Adicionamos o elemento de menor custo a Queue Open
                // System.out.println(Closed);
                Open.add(Closed.poll());
                Closed.clear();
                // System.out.println(Open);
            }
            // nao encontrou resposta
        }

        return null;
    }

    /*
     * f(n) = h(n)
     * 
     * h(n) =
     * "somatório das distâncias de cada peça ao seu lugar na configuração final"
     * 
     * (CONFIRMAR INFORMACAO ABAIXO)
     * ! Igual a A_Star_Humming, unica difenca e que so adicionamos h(n) a f(n)
     * 
     * ! NAO APLICA A CORRECAO DE MONOTONIA
     */
    public static Node Greedy_With_Heuristic_Manhattan(Node starNode, Puzzle Final) {

        // criamos um comparador customizado para Node
        // que compara pelo custo de cada no
        // (Testar mais o comparador)
        Comparator<Node> nodeCostComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {

                return n1.current.get_f_n() - n2.current.get_f_n();
            }
        };
        Queue<Node> Open = new LinkedList<Node>();
        Open.add(starNode);
        // PriorityQueue que guarda por ordem crescente do custo de f(n) dos nos
        // (Verificar se funciona!!)
        PriorityQueue<Node> Closed = new PriorityQueue<>(nodeCostComparator);
        Set<Puzzle> visited = new HashSet<>();
        visited.add(starNode.current);
        while (!(Open.isEmpty())) {
            Node n = Open.remove();
            System.out.print(n);
            if (isGoal(n, Final)) {
                System.out.println("Operadores : " + Actions(n.path));
                System.out.println("numero de passos = " + n.cost);
                System.out.println("Encontrei solucao no nivel de profundidade " + n.depth);
                System.out.println("Caminho da solucao  = \n" + n.path);
                System.out.println(" número máximo de nós armazenados simultaneamente em memória durante a execução= "
                        + generated_nodes);
                return n;
            }

            // gera os descendentes do no n
            ArrayList<Node> desc = MakeDescendants(n);

            // Acresentamos variavel que guarda o numero de nos gerados/armazenados
            generated_nodes += desc.size();

            // atribui o respetivo custo a cada um desses descendentes e inserir-los
            // na Priority Queue
            for (int i = 0; i < desc.size(); i++) {
                // Calculamos e atribuimos o respetivo valor de
                // f(n) ao descendentes
                Node aux = desc.get(i);
                int h_n = Manhattan_distance(aux, Final);
                desc.get(i).current.set_f_n(h_n);
                // criamos um novo no filho nao esquecendo de
                // adicionar ao caminho feito ate agora esse novo no filho
                // atualizando o custo , path e current(puzzle atual)
                ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
                Puzzle next = desc.get(i).current;
                newPath.add(next);
                Node filho = new Node(next, newPath, n.depth + 1);
                // Adiciona a priorityQueue caso nao tenha
                // Adicionamos a PriorityQueue
                // que vai ordena-los por ordem crescente de f(n)
                if (!(visited.contains(next))) {
                    Closed.add(filho);
                    visited.add(next);
                }
            }

            // ! Adicionamos o elemento de menor custo a Queue Open
            Open.add(Closed.poll());
        }
        // !nao encontrou resposta
        return null;
    }

    // ! Funcao que gera os descendentes e atualiza o path e depth de cada
    public static ArrayList<Node> MakeDescendants(Node n) {
        ArrayList<Node> desc = new ArrayList<>();
        // faz uma copia do estado atual do puzzle
        Puzzle aux = n.current.copy();
        // se for movimento valido
        if (aux.move_left() == true) {
            // adicionamos ao path feito ate agora
            ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
            newPath.add(aux);
            // criamos um novo no com o caminho e custo do mesmo
            desc.add(new Node(aux, newPath, n.depth + 1));
        }
        // fazemos sempre copia do puzzle atual para
        // nao interfir com as mudancas feitas dentro dos if
        Puzzle aux2 = n.current.copy();
        if (aux2.move_right() == true) {
            ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
            newPath.add(aux2);
            desc.add(new Node(aux2, newPath, n.depth + 1));
        }

        Puzzle aux3 = n.current.copy();
        if (aux3.move_up() == true) {
            ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
            newPath.add(aux3);
            desc.add(new Node(aux3, newPath, n.depth + 1));
        }

        Puzzle aux4 = n.current.copy();
        if (aux4.move_down() == true) {
            ArrayList<Puzzle> newPath = new ArrayList<>(n.path);
            newPath.add(aux4);
            desc.add(new Node(aux4, newPath, n.depth + 1));
        }
        return desc;
    }

    /*
     * Inicia o algoritmo de pesquisa com estado inicial,inicial
     * e uma string Queueingfunction que indica qual funcoa de
     * que vamos usar para gerar os nos.
     */
    public static void Start(Puzzle Inicial, Puzzle Final, String Queueingfunction) {
        if (Check_Initial_to_Final(Inicial, Final) == false) {
            System.out.println("It is impossible to reach a solution");
            return;
        }
        Queue<Node> q = new LinkedList<>();
        Node inicial = new Node();
        inicial.current = Inicial;
        inicial.depth = 0;
        inicial.path.add(Inicial);
        q.add(inicial);
        // Para contar o numero de nos gerados podemos ter uma variavel estatica que
        // ao aplicar a funcao de gerar os descendentes ele incrementa por cada
        // descendente gerado
        Node n = new Node();
        n = q.remove();
        if (isGoal(n, Final)) {
            System.out.print("Encontrada a solucao \n" + n.path + "\n");
            System.out.println(Actions(n.path));
            System.out.println("com profundidade = " + n.depth);
            return;
        }
        // todo Pesquisa em profundidade(NAO ACABA PORQUE DA STACKOVERFLOW)
        if (Queueingfunction.equals("DFS")) {
            DFS(n, Final);
            return;
        }
        // * Pesquisa em largura
        if (Queueingfunction.equals("BFS")) {
            BFS(n, Final);
            return;
        }
        // * Pesquisa iterativa em profundide
        if (Queueingfunction.equals("IDFS")) {
            DFS_Iterative_helper(n, Final, 15);
            return;
        }

        // *Pesquisa A
        if (Queueingfunction.equals("A*-misplaced")) {
            A_Star_Humming(n, Final);
            return;
        }
        if (Queueingfunction.equals("A*-Manhattan")) {
            A_Star_Manhattan(n, Final);
            return;
        }
        // *Pesquisa greedy
        if (Queueingfunction.equals("Greedy-misplaced")) {
            Greedy_With_Heuristic_Hamming(n, Final);
            return;
        }

        if (Queueingfunction.equals("Greedy-Manhattan")) {
            Greedy_With_Heuristic_Manhattan(n, Final);
            return;
        }
    }

    // !verifica se um dado no e o estado final
    public static boolean isGoal(Node n, Puzzle Final) {
        return n.current.toString().equals(Final.toString());
    }

    // * retorna os movimentos feitos para chegar a solucao
    public static String Actions(ArrayList<Puzzle> path) {
        String ans = "";

        for (int i = 1; i < path.size(); i++) {
            // obtemos as coordenadas do espaco em branco do no pai
            int x_b = path.get(i - 1).get_x();
            int y_b = path.get(i - 1).get_y();
            // obtemos as coordenadas do espaco em branco do no filho
            int x_a = path.get(i).get_x();
            int y_a = path.get(i).get_y();

            // conjunto de switch casses que indica as possiveis acoes que levaram no pai
            // para o no filho
            switch (x_b - x_a) {
                // movimento a peca branca para cima
                case 1:
                    ans += "UP ";
                    break;
                // movimento a pecao branca para baixo
                case -1:
                    ans += "DOWN ";
                    // movimento nao afeta a posicao em termos da verticalidade
                case 0:
                    break;
                default:
                    break;
            }
            switch (y_b - y_a) {
                // movimento a peca em branco para a esquerda
                case 1:
                    ans += "LEFT ";
                    break;
                // movimento a peca em branco para a direita
                case -1:
                    ans += "RIGHT ";
                    break;
                // movimento nao afeta a posicao em termos da horizontalidade
                case 0:
                    break;
                default:
                    break;
            }
        }
        return ans;
    }

    // !Para contar o numero de nos gerados/armazenados
    static int generated_nodes;

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        // Array que representa o estado inicial
        int[] a = new int[16];
        for (int i = 0; i < 16; i++)
            a[i] = stdin.nextInt();

        // Array que representa estado final
        int[] b = new int[16];
        for (int i = 0; i < 16; i++)
            b[i] = stdin.nextInt();

        Puzzle p1 = new Puzzle();
        Puzzle p2 = new Puzzle();
        p1.set_state(a);
        p2.set_state(b);

        // System.out.println(p1.is_solvable());
        // System.out.println(p2.is_solvable());
        // System.out.println(p1.get_permutation_parity());
        // System.out.println(p2.get_permutation_parity());
        // System.out.println(Check_Initial_to_Final(p1, p2) );

        // !Inicializamos o numero de nos aramazenados a 1 (que e o startNode )
        // !ESTOU A CONTAR COMO "NOS ARMAZENADOS" TODOS OS NOS GERADOS , MESMO QUE NAO
        // SEJAM "UTILIZADOS"
        generated_nodes = 1;
        // ? A linha abaixo inicia o algoritmo de pesquisa escolhido com a string
        Start(p1, p2, args[0]);

        // System.out.println(Hamming_Distance(new Node(p1,new ArrayList<>(),0), p2));
        // System.out.println(Manhattan_distance(new Node(p1,new ArrayList<>(),0), p2));
    }
}
