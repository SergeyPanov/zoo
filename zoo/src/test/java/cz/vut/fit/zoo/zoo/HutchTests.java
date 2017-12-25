package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Hutch;
import cz.vut.fit.zoo.service.HutchService;
import oracle.jdbc.pool.OracleDataSource;
import oracle.spatial.geometry.JGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HutchTests {

    @Autowired
    private HutchService hutchService;


    @Test
    public void getAllHutches(){
        System.out.println(hutchService.getAll().size());
    }

    @Test
    public void get(){
        System.out.println(hutchService.get("8798d453-fe9b-4d86-9da2-24002111fc46"));
    }

    @Test
    public void store(){
        Hutch hutch = new Hutch();
        hutch.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        50, 85,
                        60, 10}));
        hutchService.store(hutch);
    }

    @Test
    public void delete(){
        hutchService.delete("ecb1fb84-dc51-4020-88ca-274947a2f254");
    }

    @Test
    public void getAnimals() throws IOException {
        for (Animal a:
                hutchService.getAnimals("48a9ab95-559e-4710-84a6-297d06b91ad5")) {
            if (a.getPhoto().length > 0) Helper.storeImage(a.getPhoto(), a.getId());
        }
    }

    @Test
    public void getTheBiggestHutches(){
        System.out.println(hutchService.getTheGiggestHutches().size());
    }
}
