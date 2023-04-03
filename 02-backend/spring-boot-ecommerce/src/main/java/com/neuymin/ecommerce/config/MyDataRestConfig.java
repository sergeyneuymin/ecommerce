package com.neuymin.ecommerce.config;

import com.neuymin.ecommerce.entity.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;

import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;
    public EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        // disable http methods for Product: put, post, delete
        disableHttpMethods(config.getExposureConfiguration()
                .forDomainType(Product.class), theUnsupportedActions);

        // disable http methods for ProductCategory: put, post, delete
        disableHttpMethods(config.getExposureConfiguration()
                .forDomainType(ProductCategory.class), theUnsupportedActions);

        disableHttpMethods(config.getExposureConfiguration()
                .forDomainType(Country.class), theUnsupportedActions);

        disableHttpMethods(config.getExposureConfiguration()
                .forDomainType(State.class), theUnsupportedActions);

        disableHttpMethods(config.getExposureConfiguration()
                .forDomainType(Order.class), theUnsupportedActions);

        //call an internal helper method
        exposeIds(config);

        //configure cors mapping instead of cross-origin annotations in repos
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
    }

    private static void disableHttpMethods(ExposureConfigurer config, HttpMethod[] theUnsupportedActions) {
        config
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // expose entity ids

        // get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for the entities
        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }

}
