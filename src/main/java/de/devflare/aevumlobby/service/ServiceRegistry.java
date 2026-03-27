package de.devflare.aevumlobby.service;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Getter
public final class ServiceRegistry {

    private final Map<Class<? extends Service>, Service> services;
    private final Logger logger;
    private boolean initialized;

    public ServiceRegistry(final Logger logger) {
        this.services = new ConcurrentHashMap<>();
        this.logger = logger;
        this.initialized = false;
    }

    public <T extends Service> void register(final Class<T> clazz, final T service) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot register services after initialization");
        }

        if (this.services.containsKey(clazz)) {
            throw new IllegalStateException("Service " + clazz.getSimpleName() + " is already registered");
        }

        this.services.put(clazz, service);
        this.logger.info("Registered service: " + service.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> Optional<T> getService(final Class<T> clazz) {
        return Optional.ofNullable((T) this.services.get(clazz));
    }

    public void initializeAll() {
        if (this.initialized) {
            throw new IllegalStateException("Services are already initialized");
        }

        final List<Service> sortedServices = new ArrayList<>(this.services.values());
        sortedServices.sort(Comparator.comparingInt(Service::getPriority));

        this.logger.info("Initializing " + sortedServices.size() + " services...");

        for (final Service service : sortedServices) {
            try {
                service.initialize();
                this.logger.info("Initialized: " + service.getName());
            } catch (final Exception exception) {
                this.logger.severe("Failed to initialize service: " + service.getName());
                exception.printStackTrace();
            }
        }

        this.initialized = true;
        this.logger.info("All services initialized successfully");
    }

    public void shutdownAll() {
        if (!this.initialized) {
            return;
        }

        this.logger.info("Shutting down services...");

        final List<Service> sortedServices = new ArrayList<>(this.services.values());
        sortedServices.sort(Comparator.comparingInt(Service::getPriority).reversed());

        for (final Service service : sortedServices) {
            try {
                service.shutdown();
                this.logger.info("Shutdown: " + service.getName());
            } catch (final Exception exception) {
                this.logger.severe("Error shutting down service: " + service.getName());
                exception.printStackTrace();
            }
        }

        this.initialized = false;
        this.logger.info("All services shut down");
    }

    public void reloadAll() {
        this.logger.info("Reloading all services...");

        for (final Service service : this.services.values()) {
            try {
                service.reload();
                this.logger.info("Reloaded: " + service.getName());
            } catch (final Exception exception) {
                this.logger.severe("Error reloading service: " + service.getName());
                exception.printStackTrace();
            }
        }

        this.logger.info("All services reloaded");
    }

    public List<Service> getServices() {
        return new ArrayList<>(this.services.values());
    }
}
