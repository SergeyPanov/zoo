package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Plant;
import cz.vut.fit.zoo.service.AnimalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnimalsTests {
    @Autowired
    private AnimalService animalService;
    @Qualifier("simpleDateFormat")
    @Autowired
    private SimpleDateFormat dateFormat;

	@Test
    public void storeAnimal() throws IOException, ParseException {
        Path path = Paths.get("/home/xpanov00/git/fit-pdb17-proj/zoo/src/main/resources/images/Panda.jpg");
        byte[] photo = Files.readAllBytes(path);
        Animal animal = new Animal();
        animal.setName("UpdatedAnimal");
        animal.setSpecies("Species");
        animal.setFrom(dateFormat.parse("2014/12/14"));
        animal.setTo(dateFormat.parse("2015/12/14"));
        animal.setHutchId("81d3f4c8-1c76-424f-ae2b-d1a6c0bee5b9");
        animal.setPhoto(photo);
        animalService.store(animal);
    }

    @Test
    public void getOlderAnimalsThanOldestPlant(){
	    animalService.getOlderAnimalsThanOldestPlant();
    }

    @Test
    public void getAnimal() throws IOException {
	    Animal animal = animalService.get("41951cae-3ab3-4bba-8651-a35b74d69a7e");
        Helper.storeImage(animal.getPhoto(), animal.getId());
    }

    @Test
    public void getAllAnimals() throws IOException {
        for (Animal a:
                animalService.getAll()) {
            if (a.getPhoto().length > 0) Helper.storeImage(a.getPhoto(), a.getId());
        }
    }

    @Test
    public void deleteAnimal(){
	    animalService.delete("2243e71f-8062-4fd5-a931-6288b457c254");
    }



}
