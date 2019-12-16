package logis.web.rest;

import logis.domain.Loggss;
import logis.repository.LoggssRepository;
import logis.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link logis.domain.Loggss}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LoggssResource {

    private final Logger log = LoggerFactory.getLogger(LoggssResource.class);

    private static final String ENTITY_NAME = "logsLoggss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoggssRepository loggssRepository;

    public LoggssResource(LoggssRepository loggssRepository) {
        this.loggssRepository = loggssRepository;
    }

    /**
     * {@code POST  /registro} : Create a new loggss.
     *
     * @param loggss the loggss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loggss, or with status {@code 400 (Bad Request)} if the loggss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registro")
    public ResponseEntity<Loggss> createLoggss(@RequestBody Loggss loggss) throws URISyntaxException {
        log.debug("REST request to save Loggss : {}", loggss);
        if (loggss.getId() != null) {
            throw new BadRequestAlertException("A new loggss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Loggss result = loggssRepository.save(loggss);
        return ResponseEntity.created(new URI("/api/registro/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registro} : Updates an existing loggss.
     *
     * @param loggss the loggss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loggss,
     * or with status {@code 400 (Bad Request)} if the loggss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loggss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registro")
    public ResponseEntity<Loggss> updateLoggss(@RequestBody Loggss loggss) throws URISyntaxException {
        log.debug("REST request to update Loggss : {}", loggss);
        if (loggss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Loggss result = loggssRepository.save(loggss);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loggss.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /registro} : get all the registro.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registro in body.
     */
    @GetMapping("/registro")
    public List<Loggss> getAllregistro() {
        log.debug("REST request to get all registro");
        return loggssRepository.findAll();
    }

    /**
     * {@code GET  /registro/:id} : get the "id" loggss.
     *
     * @param id the id of the loggss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loggss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registro/{id}")
    public ResponseEntity<Loggss> getLoggss(@PathVariable Long id) {
        log.debug("REST request to get Loggss : {}", id);
        Optional<Loggss> loggss = loggssRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(loggss);
    }

    /**
     * {@code DELETE  /registro/:id} : delete the "id" loggss.
     *
     * @param id the id of the loggss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registro/{id}")
    public ResponseEntity<Void> deleteLoggss(@PathVariable Long id) {
        log.debug("REST request to delete Loggss : {}", id);
        loggssRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
