package br.edu.ifes.si.tpa.trabalho01;

import java.util.LinkedList;
import java.util.List;

public class FordFulkerson {
    // Quantidade de vertices
    private final int V;
    // Vertices que estão marcados na Rede de Fluxo
    private boolean[] marcado;
    // Caminho de aumento atual
    private List<ArestaFluxo> caminhoAumento;
    // Valor final da rede de fluxo
    private double valor;
    // Rede de Fluxo
    RedeFluxo rede;
    
    public FordFulkerson(RedeFluxo G, int s, int t){
        // Obtêm a quantidade de vertices
        V = G.V();
        // Inicia o valor como 0
        valor = 0;
        // Inicia a rede de fluxo
        rede = G;
        caminhoAumento = new LinkedList<ArestaFluxo>();

        while(temCaminhoDeAumento(s, t)){
            atualizarCaminhoAumento(s, t);
            
            valor += calcularCapacidadeGargalo();
        }
        
        mostrarResultado(s, t);
    }
    
    private void mostrarResultado(int s, int t){
        System.out.println(String.format("Fluxo máximo de %d para %d", s, t));
        for (int i = s;i < t-1;i++){
            for (ArestaFluxo e : rede.adj(i)){
                int w = e.para();
                if (i != w && e.fluxo() != 0){
                    System.out.println(String.format("%d->%d %.2f/%.2f", i, w, e.fluxo(), e.capacidade()));
                }
            }
        }
        
        System.out.println(String.format("Valor do fluxo máximo: %.2f", valor));
    }
    
    // Retorna o valor final do algotitmo FordFulkerson
    public double valor(){
        return valor;
    }
    
    // Procura um almento no caminho
    private boolean temCaminhoDeAumento(int s, int t) {
        // Inicia o array da variavel marcado
        marcado = new boolean[rede.V()];
        
        // Marca se ja verificou o vertice
        marcado[s] = true;
        
        LinkedList<Integer> queue
            = new LinkedList<Integer>();
        queue.add(s);
 
        // Faz a procura BFS
        while (queue.size() != 0) {
            int atual = queue.poll();
 
            for (ArestaFluxo e : rede.adj(atual)) {
                int w = e.outro(atual);
                if (marcado[w] == false && e.capacidadeResidualPara(w) > 0) {
                    // Se ja estiver no final, retorna verdadeiro
                    // e salva o caminho de aumento
                    if (w == t) {
                        caminhoAumento.add(e);
                        return true;
                    }
                    queue.add(w);
                    caminhoAumento.add(e);
                    marcado[w] = true;
                }
            }
        }
 
        // Se não achou o caminho de aumento retorna falso
        return false;
    }
    
    // Calcula a capacidade de gargalo do caminho de aumento
    private double calcularCapacidadeGargalo(){
        double gargalo = 0.0;
        
        // Calcula a capacidade de gargalo do caminho
        for (ArestaFluxo e : caminhoAumento){
            gargalo = (gargalo != 0) ? Math.min(gargalo, e.capacidade()) : e.capacidade();
        }
        
        // Atualiza o fluxo da rede
        RedeFluxo nova = new RedeFluxo(V);        
        for (ArestaFluxo e : rede.arestas()){
            boolean adicionou = false;
            for (ArestaFluxo e2 : caminhoAumento){
                if (e.equals(e2)) {
                    ArestaFluxo aresta = new ArestaFluxo(e.de(), e.para(), e.capacidade(), gargalo);
                    nova.addAresta(aresta);
                    adicionou = true;
                }
            }
            
            if (!adicionou) nova.addAresta(e);
        }
        
        // Redefine a rede atual
        rede = nova;
        
        return gargalo;
    }
    
    // Remover arestas que não fazem parte do caminho de aumento
    private void atualizarCaminhoAumento(int s, int t){;
        int ultimo_vertice = t;
        
        for (int i = caminhoAumento.size()-1;i >= s;i--){
            ArestaFluxo e = caminhoAumento.get(i);
            
            if (e.para() == ultimo_vertice){
                ultimo_vertice = e.de();
            }
            else{
                caminhoAumento.remove(e);
            }
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        RedeFluxo G = new RedeFluxo(in);
        System.out.println(G);
        
        FordFulkerson fluxomaximo = new FordFulkerson(G, 0, 7);
    }
}