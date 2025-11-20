package logic;

public class Administrador {

	private String usuario;
	private String password;
	Hospital hospital;

	public Administrador(String usuario, String password, Hospital hospital) {
		super();
		this.usuario = usuario;
		this.password = password;
		this.hospital = hospital;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}
	

}
