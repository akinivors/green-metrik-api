package com.greenmetrik.greenmetrikapi.util;

import com.greenmetrik.greenmetrikapi.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryHelper {

    /**
     * Finds an entity by its ID in the given repository or throws a ResourceNotFoundException.
     *
     * @param repository The JpaRepository to search in.
     * @param id The ID of the entity to find.
     * @param resourceType The type of the resource for the error message (e.g., "User", "Unit").
     * @param <T> The type of the entity.
     * @param <ID> The type of the ID.
     * @return The found entity.
     * @throws ResourceNotFoundException if the entity is not found.
     */
    public static <T, ID> T findOrThrow(JpaRepository<T, ID> repository, ID id, String resourceType) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    resourceType + " not found", resourceType, id
                ));
    }
}
