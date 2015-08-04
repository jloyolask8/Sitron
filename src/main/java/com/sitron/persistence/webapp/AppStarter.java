/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.webapp;

import com.sitron.persistence.entities.EstadoCaso;
import com.sitron.persistence.entities.Priority;
import com.sitron.persistence.entities.TipoAlerta;
import com.sitron.persistence.entities.enums.EnumEstadoCaso;
import com.sitron.persistence.entities.enums.EnumPrioridad;
import com.sitron.persistence.entities.enums.EnumTipoAlerta;
import com.sitron.persistence.jpa.JPAServiceFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;
import org.quartz.SchedulerException;

/**
 *
 * @author jorge
 */
//@WebListener
public class AppStarter implements ServletContextListener {

    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private static final Logger LOG = Logger.getLogger(AppStarter.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            inicializar();
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
    }
    

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        try {
            LOG.info("stopping Sitron Scheluder");
//            HelpDeskScheluder.stop();
//        } catch (SchedulerException ex) {
//            Logger.getLogger(AppStarter.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void inicializar() throws SchedulerException {
        LOG.info("Inicializando Sitron");

        JPAServiceFacade jpa = new JPAServiceFacade(utx, emf);
        verificarPrioridades(jpa);
        verificarSubEstadosCaso(jpa);
        verificarTiposAlerta(jpa);

    }
    
    private void verificarTiposAlerta(JPAServiceFacade jpaController) {
        for (EnumTipoAlerta tipoAlerta : EnumTipoAlerta.values()) {
            try {
                if (null == jpaController.find(TipoAlerta.class, tipoAlerta.getTipoAlerta().getIdalerta())) {
                    throw new NoResultException();
                }

            } catch (NoResultException ex) {
                LOG.log(Level.SEVERE, "No existe tipo alerta {0}, se creara ahora", tipoAlerta.getTipoAlerta().getNombre());
                try {
                    jpaController.persist(tipoAlerta.getTipoAlerta());
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }
    }
    
    private void verificarPrioridades(JPAServiceFacade jpaController) {
        for (EnumPrioridad enumPrioridad : EnumPrioridad.values()) {
            try {
                Priority prioridad = jpaController.find(Priority.class, enumPrioridad.getPrioridad().getPk());
                if (prioridad == null) {
                    throw new NoResultException();
                }
            } catch (NoResultException ex) {
                LOG.log(Level.SEVERE, "No existe prioridad {0}, se creara ahora", enumPrioridad.getPrioridad().getName());
                try {
                    jpaController.persist(enumPrioridad.getPrioridad());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void verificarSubEstadosCaso(JPAServiceFacade jpaController) {
        for (EnumEstadoCaso enumSubEstado : EnumEstadoCaso.values()) {
            try {
                if (null == jpaController.find(EstadoCaso.class, enumSubEstado.getEstado().getIdEstado())) {
                    throw new NoResultException();
                }
            } catch (NoResultException ex) {
                LOG.severe("No existe sub estado !!!, se creara ahora");
                try {
                    jpaController.persist(enumSubEstado.getEstado());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
