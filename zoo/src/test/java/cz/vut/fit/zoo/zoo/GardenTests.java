package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Garden;
import cz.vut.fit.zoo.model.Plant;
import cz.vut.fit.zoo.service.GardenService;
import oracle.spatial.geometry.JGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GardenTests {

    @Autowired
    private GardenService gardenService;

    @Test
    public void get(){
        System.out.println(gardenService.get("1c4e5277-f045-4dbd-9d5a-a58d2d8a2998"));
    }

    @Test
    public void getAllGardens(){
        System.out.println(gardenService.getAll().size());
    }

    @Test
    public void store(){
        Garden garden = new Garden();
        garden.setName("TEST GARDEN2");
        garden.setGeometry(new JGeometry(3, 0, new int[]{1,1003,1},
                new double[]{
                        70,85,
                        60,75,
                        60,60,
                        70,50,
                        80,60,
                        80,75,
                        70,85}));
        gardenService.store(garden);
    }

    @Test
    public void delete(){
        gardenService.delete("aeb89d51-98c7-4fe2-81c1-ca043fc3ba98");
    }

    @Test
    public void getPlants() throws IOException {
        for (Plant p :
                gardenService.getPlants("1c4e5277-f045-4dbd-9d5a-a58d2d8a2998")) {
            if (p.getPhoto().length > 0) Helper.storeImage(p.getPhoto(), p.getId());
        }
    }

    @Test
    public void gardenInContactWithWater(){
        System.out.println(gardenService.getGardentInContactWithWater().size());
    }

    @Test
    public void getGardenIdWithTheBiggestLakeInContact(){
        System.out.println(gardenService.getGardenIDWithTheBiggestLakeInContact());
    }

    @Test
    public void getIntersectGardenAndLake(){
        System.out.println(gardenService.getIntersectionWithLakes());
    }
}
