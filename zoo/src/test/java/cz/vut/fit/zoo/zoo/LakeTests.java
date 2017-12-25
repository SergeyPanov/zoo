package cz.vut.fit.zoo.zoo;

import cz.vut.fit.zoo.model.Lake;
import cz.vut.fit.zoo.service.LakeService;
import oracle.spatial.geometry.JGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LakeTests {
    @Autowired
    private LakeService lakeService;
    @Test
    public void get(){
        System.out.println(lakeService.get("b61bd3b7-77e9-42a3-bc57-4119ea492bd7"));
    }

    @Test
    public void getAllLakes(){
        System.out.println(lakeService.getAll().size());
    }

    @Test
    public void storeLake(){
        Lake lake = new Lake();
        lake.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 4},
                new double[]{
                        50, 85,
                        50, 80,
                        55, 82.5}));
        lakeService.store(lake);
    }

    @Test
    public void deleteLake(){
        lakeService.delete("b61bd3b7-77e9-42a3-bc57-4119ea492bd7");
    }

    @Test
    public void getLakeSpace(){
        System.out.println("PLOCHA =" + lakeService.getLakeSpace());
    }
}
