package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Footpath;
import cz.vut.fit.zoo.service.FootpathService;
import oracle.spatial.geometry.JGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FootpathTests {

    @Autowired
    private FootpathService footpathService;

    @Test
    public void getAllFootpathes(){
        System.out.println(footpathService.getAll().size());
    }

    @Test
    public void get(){
        System.out.println(footpathService.get("0fe7f7c0-9901-4290-8131-63f9578e4d7c"));
    }

    @Test
    public void store(){
        Footpath footpath = new Footpath();

        JGeometry jGeometry = new JGeometry(2, 0, new int[]{1,2,1},
                new double[]{
                        15,75,
                        30,60,
                        10,45,
                        10,40
        });
        footpath.setGeometry(jGeometry);
        footpathService.store(footpath);
    }

    @Test
    public void delete(){
        footpathService.delete("0fe7f7c0-9901-4290-8131-63f9578e4d7c");
    }

    @Test
    public void getIntersects(){
        System.out.println(footpathService.getIntersectFootpathes());
    }
}
