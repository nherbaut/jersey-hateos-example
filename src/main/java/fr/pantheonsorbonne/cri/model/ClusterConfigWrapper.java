package fr.pantheonsorbonne.cri.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClusterConfigWrapper {
	public List<ClusterConfig> getClusterConfig() {
		return clusterConfig;
	}

	public void setClusterConfig(List<ClusterConfig> clusterConfig) {
		this.clusterConfig = clusterConfig;
	}

	@Override
	public String toString() {
		return "ClusterConfigWrapper [clusterConfig=" + clusterConfig + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	List<ClusterConfig> clusterConfig = new ArrayList<>();
}
