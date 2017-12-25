package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Plant;
import cz.vut.fit.zoo.service.PlantService;
import oracle.spatial.geometry.JGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlantTests {

    @Autowired
    private PlantService plantService;

    @Qualifier("simpleDateFormat")
    @Autowired
    private SimpleDateFormat dateFormat;

    @Test
    public void kill(){
        plantService.kill("73be7b99-b769-499e-8b3f-64b0b3e0133a");
    }

    @Test
    public void getAllPlants() throws IOException {

        for (Plant p :
                plantService.getAll()) {
            if (p.getPhoto().length > 0) Helper.storeImage(p.getPhoto(), p.getId());
        }

        System.out.println(plantService.getAll().size());
    }

    @Test
    public void getAlive(){

        for (Plant p :
                plantService.getAlive()
                ){
            assert p.getTo() == null;
        }
    }

    @Test
    public void getHistory(){
        plantService.getHistory("22feb159-a5cb-4331-a103-ac741231c3af");
    }

    @Test
    public void getAliveinPeriod(){
        plantService.getAliveinPeriod(new Date(), new Date());
    }
    @Test
    public void getAllinPeriod() throws ParseException {
        for (Plant plant:
                plantService.getAllInPeriod(new Date(), new Date())){
            System.out.println(plant);
        }
    }

    @Test
    public void get() throws IOException {
        Plant plant = plantService.get("2b245eb4-33c4-4c29-9f41-324aad2d7326");
        Helper.storeImage(plant.getPhoto(), plant.getId());
    }

    @Test
    public void store() throws IOException, ParseException {
        Plant plant = new Plant();
        plant.setName("FORFUN");
        plant.setType("TEST TYPE 2");
        plant.setGeometry(new JGeometry(50.0, 75.0, 0));

        // plant.setTo(dateFormat.parse("2017/12/19"));
        Path path = Paths.get("/home/xpanov00/git/fit-pdb17-proj/zoo/src/main/resources/images/Plant.png");
        byte[] photo = Files.readAllBytes(path);

        plant.setPhoto(photo);

        plant.setGardenId("d5e7d647-1c57-498d-a94b-a5ea06e9128d");

        plantService.store(plant);
    }

    @Test
    public void update() throws ParseException {
        Plant plant = plantService.get("2a4e26e3-2f7f-4878-bed2-8d51d9b36b8c");

        plant.setTo(dateFormat.parse("2017/12/14"));

        plantService.update(plant);
    }

    @Test
    public void getListOfPlantWithSameLiveTime(){
        plantService.getListOfPlantWithSameLiveTime().forEach((k, v) -> {
            System.out.println(k + " " + v.size());

        });
    }
    @Test
    public void getTheOldests(){
        plantService.getTheOldests();
    }
    @Test
    public void delete(){
        plantService.delete("44396660-6153-47f5-8423-0f3f59d1a81d");
    }
}
