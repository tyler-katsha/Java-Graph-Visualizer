void main(){
    
    Graph graph = new Graph(CustomPanel.getSizeG());
    CustomPanel customPanel = new CustomPanel(graph).setGui();

    TopControlPanel topControlPanel = new TopControlPanel(customPanel)
            .setGui();
    
    ControlPanel controlPanel = new ControlPanel(customPanel,graph)
            .setGui();

    var _ = new MainFrame(customPanel,controlPanel,topControlPanel).setGui();




}
