package br.edu.ifes.si.tpa.trabalho01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;


public class FordFulkerson {
    private static final String NEWLINE = System.getProperty("line.separator");
    
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

        while(temCaminhoDeAumento(s, t)){
            atualizarCaminhoAumento(s, t);
            
            valor += calcularCapacidadeGargalo();
        }
    }
    
    // Retorna o valor final do algotitmo FordFulkerson
    public double valor(){
        return valor;
    }
    
    // Procura um almento no caminho
    private boolean temCaminhoDeAumento(int s, int t) {
        caminhoAumento = new ArrayList<>();
        // Inicia o array da variavel marcado
        marcado = new boolean[rede.V()];
        
        // Marca se ja verificou o vertice
        boolean[] verificado = new boolean[rede.V()];
        marcado[s] = true;
        
        // Vertice atual
        int atual = s;
        int vertice = 0;
        while(!marcado[t]){
            int novo_atual = 0;
            boolean temProximo = true;
            for (ArestaFluxo e : rede.adj(atual)){
                vertice = e.outro(atual); // V = 1º vert n verifica 
                for (ArestaFluxo e2 : rede.adj(atual)){
                    int w = e2.outro(atual); //e.outro(atual);
                    boolean a = e2.capacidadeResidualPara(w) > 0;
                    double b = e2.capacidadeResidualPara(w);
                    if (!verificado[w] && (e2.capacidadeResidualPara(w) > 0)){ // Verificar a viabilidade 
                        if (!marcado[w]){
                            marcado[w] = true;
                            caminhoAumento.add(e2);
                        }
                    }
                }
                verificado[atual] = true;
                
                for (ArestaFluxo e2 : rede.adj(vertice)){
                    int w = e2.outro(vertice); //e.outro(atual);
                    boolean a = e2.capacidadeResidualPara(w) > 0;
                    double b = e2.capacidadeResidualPara(w);
                    if (!verificado[w] && (e2.capacidadeResidualPara(w) > 0)){ // Verificar a viabilidade 
                        if (!marcado[w]){
                            marcado[w] = true;
                            caminhoAumento.add(e2);
                        }
                    }
                }
                verificado[vertice] = true;// Atual = primeiro V verificado
            }
            
            atual++;
        }
        
        return marcado[t];
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
    private void atualizarCaminhoAumento(int s, int t){
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
    
    // Verifica a viabilidade do caminho de aumento escolhido
    private boolean verificarViabilidade(ArestaFluxo a, ArestaFluxo b){ 
        return true;
    }
    
    // Mostra o caminho de aumento no console
    private void mostraCaminhoDeAumento(Queue caminho){
        int i = 0;
        System.out.print(String.format("%d: ", i));
        while (!caminho.isEmpty()){
            System.out.print(String.format("%d", caminho.remove()));
            
            if (caminho.size() >= 1) System.out.print("->");
        }
        System.out.print("\n");
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        RedeFluxo G = new RedeFluxo(in);
        System.out.println(G);
        
        FordFulkerson maxflow = new FordFulkerson(G, 0, 7);
    }
}