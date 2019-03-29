/**
 * Uma solução simples para o problema das cargas usando o algoritmo
 * de tempera simulada. 
 * 
 * @author Igor Sgorla Brehm
 */
import java.util.Random;
public class Work_Hours_Problem {
    public int[] work_hours;
    public int total_tasks;
    public String result1 = "";
    public String result2 = ""; 
    
    // Inicializador do objeto
    public Work_Hours_Problem(){
        this.work_hours = new int[]{10,5,5,20,5,15,1,3,4,18,2,16,9,3,3,2,1,7,1,2};
        this.total_tasks = this.work_hours.length;
        System.out.println("Iniciada a resolucao...");
        simulated_Annealing();
    }
    
    // Este metodo compara a diferenca de horas a serem trabalhadas entre as duas pessoas
    public int evaluated_Distribution(int[] vector){
        int p1 = 0; 
        int p2 = 0;
        for(int i = 0; i < total_tasks/2; i++) {
            p1 = p1 + vector[i];
        }
        for(int i = total_tasks/2; i < total_tasks; i++) {
            p2 = p2 + vector[i];
        }
        if(p1 < p2){
            return p2 - p1;
        }
        else if(p1 > p2){
            return p1 - p2;
        }
        else{
            return p1 - p2;
        }    
    }
    
    // Este metodo tenta encontrar uma distribuicao melhor de horas entre as duas pessoas
    public void find_Successor(double T){
        Random index = new Random();
        int j = 0;
        int i = 0;
        while(i == j){
            j = index.nextInt(total_tasks);
            i = index.nextInt(total_tasks);    
        }
        int[] candidate = work_hours;
        int aux = candidate[j];
        candidate[j] = candidate[i];
        candidate[i] = aux;
        int actual = evaluated_Distribution(work_hours);
        int next = evaluated_Distribution(candidate);
        double random_dropout = Math.exp(-(next-actual)/T);
        if(actual > next){
            work_hours = candidate;
            return;
        }
        else if(random_dropout > 0) {
            work_hours = candidate;
            return;
        }
        else{
            return;
        }
    }
    
    // Metodo que simula o algoritmo de tempera simulada
    public void simulated_Annealing(){
        double T = 300;
        for(int t = 1; t < 10000; t++){  
            find_Successor(T);
            if(evaluated_Distribution(work_hours) == 0){
                break;
            }    
            T = T*0.99;
        }
        result1 = toString(true);
        result2 = toString(false);
    }
    
    // Metodo padrao de transformacao do output da solucao...
    // Para uso na execucao da solucao em um terminal.
    public String toString(boolean p1){
        String exit_text = "";
        if(p1 == true){
            for(int i = 0; i < total_tasks/2; i++){
                exit_text = exit_text + work_hours[i]+" |";
            }
            return exit_text;
        }
        else{
            for(int i = total_tasks/2; i < total_tasks; i++){
                exit_text = exit_text + work_hours[i]+" |";
            }
            return exit_text;    
        }
        
    }
    
    // Getter simples do String resultado do problema
    public String getResult(boolean p1){
        if(p1 == true){
            return this.result1;
        }
        else{
            return this.result2; 
        }    
    }  
    
    public int getFinalDiff(){
        return this.evaluated_Distribution(work_hours);
    }    
    
    // Metodo Main da solucao
    public static void main(String args[]){
       Work_Hours_Problem problem = new Work_Hours_Problem();
       System.out.println("Ordenacao final P1: "+ problem.getResult(true));
       System.out.println("Ordenacao final P2: "+ problem.getResult(false));
       System.out.println("Diferenca entre pessoas final: "+ problem.getFinalDiff());
    }
}
