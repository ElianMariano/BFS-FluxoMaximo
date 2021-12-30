package br.edu.ifes.si.tpa.trabalho01;


public class FordFulkerson {
    private static final double FLOATING_POINT_EPSILON = 1E-11;
    private final int V;
    private boolean[] marcado;
    private double valor;
    
    public FordFulkerson(RedeFluxo G, int s, int t){
        V = G.V();
    }
    
    public double valor(){
        return valor;
    }
    
    public boolean emCorte(int v){
        validar(v);
        return marcado[v];
    }
    
    private void validar(int v){
        if (v < 0 || v >= V)
            throw new IllegalArgumentException(String.format("Vertice %d não está entre 0 e %d", v, (V-1)));
    }
    
    private double excesso(RedeFluxo G, int v){
        double excesso = 0.0;
        for (ArestaFluxo e : G.adj(v)){
            if (v == e.de()) excesso -= e.fluxo();
            else             excesso += e.fluxo();
        }
        return excesso;
    }
}