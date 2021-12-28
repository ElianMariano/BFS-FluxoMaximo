package br.edu.ifes.si.tpa.trabalho01;

public class ArestaFluxo {
    private static final double FLOATING_POINT_EPSILON = 1E-10;

    private final int v;               // de
    private final int w;               // para 
    private final double capacidade;   // capacidade
    private double fluxo;              // fluxo

    public ArestaFluxo(int v, int w, double capacidade) {
        this.v           = v;
        this.w           = w;  
        this.capacidade  = capacidade;
        this.fluxo       = 0.0;
    }

    public ArestaFluxo(int v, int w, double capacidade, double fluxo) {
        this.v           = v;
        this.w           = w;  
        this.capacidade  = capacidade;
        this.fluxo       = fluxo;
    }

    public ArestaFluxo(ArestaFluxo e) {
        this.v         = e.v;
        this.w         = e.w;
        this.capacidade  = e.capacidade;
        this.fluxo      = e.fluxo;
    }

    public int de() {
        return v;
    }  

    public int para() {
        return w;
    }  

    public double capacidade() {
        return capacidade;
    }

    public double fluxo() {
        return fluxo;
    }

    public int outro(int vertice) {
        if      (vertice == v) return w;
        else                  return v;
    }

    public double capacidadeResidualPara(int vertice) {
        if      (vertice == v)   return fluxo;                // aresta de retorno
        else  /*(vertice == w)*/ return capacidade - fluxo;   // aresta para frente
    }

    public void addFluxoResidualPara(int vertice, double delta) {
        if      (vertice == v)   fluxo -= delta;              // aresta de retorno
        else  /*(vertice == w)*/ fluxo += delta;              // aresta para frente
    }

    public String toString() {
        return v + "->" + w + " " + fluxo + "/" + capacidade;
    }

    public static void main(String[] args) {
        ArestaFluxo e = new ArestaFluxo(12, 23, 4.56);
        System.out.println(e);
    }

}