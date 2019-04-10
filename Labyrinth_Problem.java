/**
 * Uma solução para o problema do labirinto usando o algoritmo
 * de tempera simulada. 
 * 
 * @author Igor Sgorla Brehm, Alexandre Bing e Vinicius Parmeggiani
 */
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class Labyrinth_Problem {
    private String[][] labyrinth;
    private int dimension;
    private String[] best_path;
    private int best_score;
    private int trouble_index;
    private int stuck_counter;
    
    // Inicializador do objeto
    public Labyrinth_Problem() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo2.txt");
        Scanner in = new Scanner(file);
        this.dimension = in.nextInt();
        this.trouble_index = 0;
        this.stuck_counter = 0;
        this.best_path = new String[dimension*dimension];
        this.best_path = initialize_path(best_path,"start");
        this.labyrinth = new String[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
            }
        }
        in.close();
        this.best_score = evaluate_Path(best_path,"Best Path");
        System.out.println("Iniciada a execucao do algoritmo...\n");
        System.out.println("Labirinto a ser resolvido:");
        print_Labyrinth();
        simulated_Annealing();
    }
    
    // Metodo que inicializa os valores do vetor de direcoes
    private String[] initialize_path(String[] path, String command){
        Random random = new Random();
        int j = 0;
        if(command.equals("improve")){ // candidate
            for(int i = 0; i < trouble_index; i++){
                path[i] = best_path[i];
            }
            for(int i = trouble_index; i < path.length; i++){
                j = random.nextInt(4);
                if(j == 1){ // 1
                    path[i] = "L";
                }
                else if(j == 2){ // 2
                    path[i] = "R";
                }
                else if(j == 3){ // 3
                    path[i] = "U";
                }
                else{ // 4
                    path[i] = "D";
                }
            }
        }
        else{
            for(int i = 0; i < path.length; i++){
                j = random.nextInt(4);
                if(j == 1){ // 1
                    path[i] = "L";
                }
                else if(j == 2){ // 2
                    path[i] = "R";
                }
                else if(j == 3){ // 3
                    path[i] = "U";
                }
                else{ // 4
                    path[i] = "D";
                }
            }
        }
        return path;
    }
    
    // Este metodo calcula o quao boa e a sequencia de direcoes sendo avaliada
    /*
     * Score: 
     * 1. Quantos passos da sem bater em nada: Cada passo aumenta score em 1% do total de posicoes do labirinto
     * 2. Penalizar movimentos em 'circulo': Cada comportamento circular detectado penaliza em 10% do total de posicoes do labirinto
     * 3. Chega na saida? Ganha muito score: automaticamente recebe 80% do total de posicoes no score (sem levar em conta bonus e penalidades).
     */
    private int evaluate_Path(String[] path,String name){
        int score = 0;
        int current_x = 0;
        int current_y = 0;
        print_Path(path,"full");
        System.out.println("Avaliando "+name);
        for(int k = 0; k < path.length; k++){
            int[] feedback = new int[3];
            System.out.println("Movendo para: "+path[k]);
            System.out.println("Posicao atual: "+labyrinth[current_x][current_y]+","+current_x+","+current_y);
            feedback = move(path[k], current_x, current_y);
            current_x = feedback[1];
            current_y = feedback[2];
            if(feedback[0] == 0){
                System.out.println("BATEU! "+trouble_index);
                if(name.equals("Best Path")){
                    trouble_index = k;
                    System.out.println("NEW TROUBLE INDEX: "+trouble_index);
                }
                break; // 0 bateu na parede
            }
            else if(feedback[0] == 1){
                if(detect_circle(path,k,current_x,current_y) == true){ // 2 movimento em circulo
                    score = score - 4;
                    System.out.println("ANDOU EM CIRCULO!");
                }
                else{ // 1 movimento bom
                    score = score - 1; 
                    System.out.println("ANDOU COM SUCESSO!");
                }
            }
            else{ // 3 chegou na saida
                System.out.println("CHEGOU NA SAIDA!");
                trouble_index = k;
                score = score + (dimension*dimension*dimension);
                // *********************************************************************
                // TODO: agora que chegamos na saida, o score deve funcionar diferente
                // ja que agora queremos encontrar o menor caminho ate a saida
                // *********************************************************************
                break;
            }
        }
        return score;
    }
    
    // Metodo que realiza o movimento atual dentro do labirinto
    private int[] move(String movement, int position_x, int position_y){
        int[] exit_status;
        int x = position_x;
        int y = position_y;
        if(movement.equals("U")){
            boolean valid = check_move_validity("U",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x-1][position_y].equals("0")|| labyrinth[position_x-1][position_y].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x - 1;
                    y = position_y;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x-1][position_y].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x - 1;
                    y = position_y;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else if(movement.equals("D")){
            boolean valid = check_move_validity("D",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x+1][position_y].equals("0")|| labyrinth[position_x+1][position_y].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x + 1;
                    y = position_y;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x+1][position_y].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x + 1;
                    y = position_y;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else if(movement.equals("L")){
            boolean valid = check_move_validity("L",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x][position_y-1].equals("0")|| labyrinth[position_x][position_y-1].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y-1;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x][position_y-1].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y-1;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else{
            boolean valid = check_move_validity("R",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x][position_y+1].equals("0")|| labyrinth[position_x][position_y+1].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y+1;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x][position_y+1].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y+1;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
    }
    
    
    // Metodo que verifica a validade do movimento, isto e, se nao vai sair fora do labirinto
    private boolean check_move_validity(String move,int pos_x,int pos_y){
        if(move.equals("U")){ //Up
            if(pos_x <= 0){
                return false;
            }
            else{
                return true;
            }
        }
        else if(move.equals("D")){ //Down
            if(pos_x >= dimension-1){
                return false;
            }
            else{
                return true;
            }
        }
        else if(move.equals("L")){ //Left
            if(pos_y <= 0){
                return false;
            }
            else{
                return true;
            }
        }
        else{ //Right
            if(pos_y >= dimension-1){
                return false;
            }
            else{
                return true;
            }
        }
    }
    
    // Este metodo tenta identificar movimentos em circulo
    private boolean detect_circle(String[] path, int moves, int x, int y){
        if(moves == 0){
            return false;
        }
        else{
            if(path[moves-1].equals("L") && path[moves].equals("R")){
                return true;   
            }
            else if(path[moves-1].equals("R") && path[moves].equals("L")){
                return true;
            }
            else if(path[moves-1].equals("D") && path[moves].equals("U")){
                return true;
            }
            else if(path[moves-1].equals("U") && path[moves].equals("D")){
                return true;    
            }
            else if(labyrinth[x][y].equals("$")){
                return true;
            }
            else{
                return false;
            }
        }
    }
    
    // Este metodo tenta encontrar um estado vizinho (combinacao de direcoes com apenas 2 direcoes de diferenca do estado atual)
    // que seja melhor que o estado atual.
    /*
     * Direcoes possiveis:
     * 1. L -> Esquerda
     * 2. R -> Direita
     * 3. U -> Cima
     * 4. D -> Baixo
     * 
     * Tipos de posicoes no labirinto:
     * 1. E -> Entrada
     * 2. S -> Saida
     * 3. 0 -> Passagem
     * 4. 1 -> Parede
     */
    public void find_Successor(int T) throws FileNotFoundException{
        Random index = new Random();
        String aux = "";
        String[] candidate = new String[dimension*dimension];
        candidate = initialize_path(candidate,"improve");
        int next = evaluate_Path(candidate,"Candidate");
        print_Labyrinth();
        reset_Labyrinth();
        int random_dropout = index.nextInt(5000)-T;
        if(best_score > next || next > 0){
            stuck_counter = 0;
            best_path = candidate;
            best_score = evaluate_Path(best_path,"Best Path");
            return;
        }
        else if(random_dropout <= 0 || stuck_counter > 20) {
            stuck_counter = 0;
            System.out.println("RANDOM DROPOUT! ");
            best_path = initialize_path(best_path,"start");
            best_score = evaluate_Path(best_path,"Best Path");
            return;
        }
        else{
            stuck_counter = stuck_counter + 1;
            return;
        }
    }
    
    // Metodo que simula o algoritmo de tempera simulada
    public void simulated_Annealing() throws FileNotFoundException{
        int T = 5000;
        for(int t = 1; t < 10000; t++){
            find_Successor(T);
            if(best_score > 0){
                System.out.println("Solucao satisfatoria encontrada: "+best_score);
                print_Path(best_path,"partial");
                print_Labyrinth();
                break;
            }    
            else{
                System.out.println("SCORE ATUAL: "+best_score);
                System.out.println("**********************************************************************************");
            }
            T = T-1;
        }
        reset_Labyrinth();
        a_Algorythm();
    }
    
    // Implementacao do algoritmo A*
    public void a_Algorythm(){
        System.out.println("**********************************************************************************");
        System.out.println("Executando algoritmo A*: ");
        int aux = 0;
        int x = 0;
        int y = 0;
        int e_x = 0;
        int e_y = 0;
        // encontra posicao da saida
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(labyrinth[i][j].equals("S")){
                    e_x = i;
                    e_y = j;
                }
            }
        }
        String[] path = new String[dimension*dimension];
        String[] data = new String[4];
        // inicializa o caminho com valores invalidos
        for(int k = 0; k < path.length; k++){
            path[k] = "F";
        }
        for(int i = 0; i < path.length; i++){
            data = find_Move(x,y,e_x,e_y);
            path[i] = data[0];
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            if(data[3].equals("exit")){
                break;
            }
        }
        System.out.println("Solucao otima encontrada usando o algoritmo A*: ");
        for(int j = 0; j < path.length-1; j++){
            if(path[j+1].equals("F")){
                aux = j;
                break;
            }
            System.out.print(path[j]+" -> ");
        }
        System.out.println(path[aux]);
        print_Labyrinth();
    }
    
    // Metodo que utiliza a heuristica para encontrar a melhor direcao a se seguir
    private String[] find_Move(int x, int y, int exit_x, int exit_y){
        System.out.println("Movendo de: "+x+","+y+". Tentando chegar a: "+exit_x+","+exit_y);
        print_Labyrinth();
        String[] result = new String[4];
        String exit = "continue";
        int left = dimension * dimension;
        int right = dimension * dimension;
        int up = dimension * dimension;
        int down = dimension * dimension;
        // encontrar as opcoes de movimento (quais as posicoes adjacentes que sao 0)
        // verificar as distancias que se ficaria da saida em cada opcao de movimento
        // atualizar os valores para x e y, retornar o movimento feito
        boolean valid = check_move_validity("L",x,y);
        if(valid == true){
            if(labyrinth[x][y-1].equals("0")){
                left = distance(exit_x,x,exit_y,y-1);
            }
            if(labyrinth[x][y-1].equals("S")){
                exit = "exit";
                result[0] = "L";
                labyrinth[x][y] = "$";
                result[1] = Integer.toString(x);
                result[2] = Integer.toString(y-1);
                result[3] = exit;
                return result;
            }
        }
        valid = check_move_validity("R",x,y);
        if(valid == true){
            if(labyrinth[x][y+1].equals("0")){
                right = distance(exit_x,x,exit_y,y+1);
            }
            if(labyrinth[x][y+1].equals("S")){
                exit = "exit";
                result[0] = "R";
                labyrinth[x][y] = "$";
                result[1] = Integer.toString(x);
                result[2] = Integer.toString(y+1);
                result[3] = exit;
                return result;
            }
        }
        valid = check_move_validity("U",x,y);
        if(valid == true){
            if(labyrinth[x-1][y].equals("0")){
                up = distance(exit_x,x-1,exit_y,y);
            }
            if(labyrinth[x-1][y].equals("S")){
                exit = "exit";
                result[0] = "U";
                labyrinth[x][y] = "$";
                result[1] = Integer.toString(x-1);
                result[2] = Integer.toString(y);
                result[3] = exit;
                return result;
            }
        }
        valid = check_move_validity("D",x,y);
        if(valid == true){
            if(labyrinth[x+1][y].equals("0")){
                down = distance(exit_x,x+1,exit_y,y);
            }
            if(labyrinth[x+1][y].equals("S")){
                exit = "exit";
                result[0] = "D";
                labyrinth[x][y] = "$";
                result[1] = Integer.toString(x+1);
                result[2] = Integer.toString(y);
                result[3] = exit;
                return result;
            }
        }
        // pegar o menor
        // atualizar os valores para x e y, retornar o movimento feito
        if(left <= right && (left <= up && (left <= down))){
            result[0] = "L";
            labyrinth[x][y] = "$";
            result[1] = Integer.toString(x);
            result[2] = Integer.toString(y-1);
            result[3] = exit;
        }
        else if(right <= left && (right <= up && (right <= down))){
            result[0] = "R";
            labyrinth[x][y] = "$";
            result[1] = Integer.toString(x);
            result[2] = Integer.toString(y+1);
            result[3] = exit;
        }
        else if(up <= left && (up <= right && (up <= down))){
            result[0] = "U";
            labyrinth[x][y] = "$";
            result[1] = Integer.toString(x-1);
            result[2] = Integer.toString(y);
            result[3] = exit;
        }
        else{
            result[0] = "D";
            labyrinth[x][y] = "$";
            result[1] = Integer.toString(x+1);
            result[2] = Integer.toString(y);
            result[3] = exit;
        }
        return result;
    }
    
    // Calcula a distancia em quadrados entre uma posicao e outra
    private int distance(int x1, int x2, int y1, int y2){
        if(x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0){
            return dimension*dimension;   
        }
        else if(x1 > x2){
            if(y1 > y2){
                return (x1 - x2) + (y1 - y2);
            }
            else{
                return (x1 - x2) + (y2 - y1);
            }
        }
        else{
            if(y1 > y2){
                return (x2 - x1) + (y1 - y2);
            }
            else{
                return (x2 - x1) + (y2 - y1);
            }
        }
    }
    
    // Reseta o labirinto
    private void reset_Labyrinth() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo2.txt");
        Scanner in = new Scanner(file);
        dimension = in.nextInt();
        labyrinth = new String[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
            }
        }
        in.close();
    }
    
    //Metodo para escrever na tela o labirinto atual
    public void print_Labyrinth(){
        System.out.print(" ");
        for(int i = 0; i < dimension-1; i++){
            System.out.print("___");
        }
        System.out.print("_");
        System.out.println("");
        for(int i = 0; i < dimension; i++){
            System.out.print("|");
            for(int j = 0; j < dimension; j++){
                if(j == dimension - 1){
                    System.out.print(" "+labyrinth[i][j]);
                }
                else if(j == 0){
                    System.out.print(labyrinth[i][j]+" ");
                }
                else{
                    System.out.print(" "+labyrinth[i][j]+" ");    
                }
            }
            System.out.print("|");
            System.out.println("");
        }
        System.out.println("");
    }
    
    //Metodo para escrever na tela uma sequencia de direcoes dentro do labirinto
    public void print_Path(String[] path,String mode){
        if(mode.equals("full")){
            for(int i = 0; i < path.length-1; i++){
            System.out.print(path[i]+" -> ");    
            }
            System.out.print(path[path.length-1]);
            System.out.println("\n");
        }
        else{
            for(int i = 0; i < trouble_index; i++){
            System.out.print(path[i]+" -> ");    
            }
            System.out.print(path[trouble_index]);
            System.out.println("\n");
        }
    }
    
    // Metodo Main da solucao
    public static void main(String args[]) throws FileNotFoundException{
       Labyrinth_Problem problem = new Labyrinth_Problem();
    }
}
