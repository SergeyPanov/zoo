package cz.vut.fit.zoo.configuration;

import cz.vut.fit.zoo.service.*;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Holds all useful services for convenience.
 * @author xpanov00
 */
public class ServicesHolder {

    private LakeService lakeService;
    private HutchService hutchService;
    private AnimalService animalService;
    private GardenService gardenService;
    private PlantService plantService;
    private FootpathService footpathService;
    private OracleDataSource dataSource;

    private static ServicesHolder ourInstance = new ServicesHolder();

    public static ServicesHolder getInstance() {
        return ourInstance;
    }

    private ServicesHolder() {
    }

    public LakeService getLakeService() {
        return lakeService;
    }

    public void setLakeService(LakeService lakeService) {
        this.lakeService = lakeService;
    }

    public HutchService getHutchService() {
        return hutchService;
    }

    public void setHutchService(HutchService hutchService) {
        this.hutchService = hutchService;
    }

    public AnimalService getAnimalService() {
        return animalService;
    }

    public void setAnimalService(AnimalService animalService) {
        this.animalService = animalService;
    }

    public GardenService getGardenService() {
        return gardenService;
    }

    public void setGardenService(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    public PlantService getPlantService() {
        return plantService;
    }

    public void setPlantService(PlantService plantService) {
        this.plantService = plantService;
    }

    public FootpathService getFootpathService() {
        return footpathService;
    }

    public void setFootpathService(FootpathService footpathService) {
        this.footpathService = footpathService;
    }

    public OracleDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(OracleDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
