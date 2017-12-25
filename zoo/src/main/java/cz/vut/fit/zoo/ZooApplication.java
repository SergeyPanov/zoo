package cz.vut.fit.zoo;

import cz.vut.fit.zoo.gui.GUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ZooApplication implements CommandLineRunner {

	@Autowired
	private GUI gui;


	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ZooApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String ... args) throws Exception{
		gui.gui(args);
	}

}
