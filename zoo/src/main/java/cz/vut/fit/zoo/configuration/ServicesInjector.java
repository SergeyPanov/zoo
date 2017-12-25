package cz.vut.fit.zoo.configuration;

import cz.vut.fit.zoo.service.*;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Inject services in ServicesHolder.
 * @author xpanov00
 */
@Component
public class ServicesInjector {
    private final LakeService lakeService;

    private final HutchService hutchService;

    private final AnimalService animalService;

    private final GardenService gardenService;

    private final PlantService plantService;

    private final FootpathService footpathService;

    private final OracleDataSource dataSource;

    @Autowired
    public ServicesInjector(LakeService lakeService, HutchService hutchService, AnimalService animalService, GardenService gardenService, PlantService plantService, FootpathService footpathService, OracleDataSource dataSource) {
        this.lakeService = lakeService;
        this.hutchService = hutchService;
        this.animalService = animalService;
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.footpathService = footpathService;
        this.dataSource = dataSource;
        inject();

    }

    private void inject(){
        ServicesHolder.getInstance().setLakeService(lakeService);
        ServicesHolder.getInstance().setHutchService(hutchService);
        ServicesHolder.getInstance().setAnimalService(animalService);
        ServicesHolder.getInstance().setGardenService(gardenService);
        ServicesHolder.getInstance().setPlantService(plantService);
        ServicesHolder.getInstance().setFootpathService(footpathService);
        ServicesHolder.getInstance().setDataSource(dataSource);

    }
}
