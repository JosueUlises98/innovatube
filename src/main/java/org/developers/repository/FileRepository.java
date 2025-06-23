package org.developers.repository;

import org.developers.model.entities.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    // Búsqueda por nombre de archivo
    Optional<File> findByFileName(String fileName);

    // Búsqueda por hash del archivo
    Optional<File> findByFileHash(String fileHash);

    // Listar archivos por tipo
    List<File> findByFileType(String fileType);

    // Buscar archivos por estado
    List<File> findByStatus(File.FileStatus status);

    // Buscar archivos por userId
    Page<File> findByUserId(Long userId, Pageable pageable);

    // Buscar archivos por userId y estado
    Page<File> findByUserIdAndStatus(Long userId, File.FileStatus status, Pageable pageable);

    // Buscar archivos por extensión
    List<File> findByExtension(String extension);

    // Contar archivos por userId
    long countByUserId(Long userId);

    // Buscar archivos creados entre fechas
    List<File> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar archivos por tipo y estado
    List<File> findByFileTypeAndStatus(String fileType, File.FileStatus status);

    // Buscar archivos que contengan cierto nombre (case insensitive)
    @Query("SELECT f FROM File f WHERE LOWER(f.fileName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<File> findByFileNameContainingIgnoreCase(@Param("name") String name);

    // Buscar archivos por userId y nombre de archivo que contenga
    @Query("SELECT f FROM File f WHERE f.user.id = :userId AND LOWER(f.fileName) LIKE LOWER(CONCAT('%', :fileName, '%'))")
    List<File> findByUserIdAndFileNameContainingIgnoreCase(@Param("userId") Long userId, @Param("fileName") String fileName);

    // Buscar archivos por tamaño mayor que
    List<File> findBySizeGreaterThan(Long size);

    // Buscar archivos por tamaño menor que
    List<File> findBySizeLessThan(Long size);

    // Verificar si existe un archivo por nombre y userId
    boolean existsByFileNameAndUserId(String fileName, Long userId);

    // Eliminar archivos por estado
    void deleteByStatus(File.FileStatus status);

    // Eliminar archivos por userId
    void deleteByUserId(Long userId);

    // Buscar archivos actualizados después de una fecha
    List<File> findByUpdateDateAfter(LocalDateTime date);

    // Consulta personalizada para archivos sin thumbnail por userId
    @Query("SELECT f FROM File f WHERE f.thumbnail IS NULL AND f.user.id = :userId")
    List<File> findFilesWithoutThumbnailByUserId(@Param("userId") Long userId);

    // Buscar archivos por userId y tipo de archivo
    List<File> findByUserIdAndFileType(Long userId, String fileType);

    // Buscar archivos por userId y estado ordenados por fecha de creación
    @Query("SELECT f FROM File f WHERE f.user.id = :userId AND f.status = :status ORDER BY f.creationDate DESC")
    List<File> findByUserIdAndStatusOrderByCreationDateDesc(@Param("userId") Long userId, @Param("status") File.FileStatus status);

}
