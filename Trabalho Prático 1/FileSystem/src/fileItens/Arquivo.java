package fileItens;

public class Arquivo extends Item {

	private String conteudo;
	
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	

	public Arquivo(Diretorio pai, String nome, String conteudo) {
		super(nome, pai);
		this.setConteudo(conteudo);
		this.setPermissao("-rwxrwxrwx");
	}
	
	public Arquivo(Diretorio pai, Arquivo molde) {
		super(molde.getNome(), pai);
		this.setConteudo(molde.getConteudo());
		this.setPermissao(molde.getPermissao());
	}
	
	public Arquivo(Diretorio pai, String nome, Arquivo molde) {
		super(nome, pai);
		this.setConteudo(molde.getConteudo());
		this.setPermissao(molde.getPermissao());
	}
}
