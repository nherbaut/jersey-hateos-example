package fr.pantheonsorbonne.cri.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterConfig {

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "ClusterConfig [host=" + host + ", ip=" + ip + ", user=" + user + ", brokerURL=" + brokerURL + "]";
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}

	String host;
	String ip;
	String user;
	String brokerURL;
}
