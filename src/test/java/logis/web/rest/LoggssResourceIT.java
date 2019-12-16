package logis.web.rest;

import logis.LogsApp;
import logis.domain.Loggss;
import logis.repository.LoggssRepository;
import logis.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static logis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LoggssResource} REST controller.
 */
@SpringBootTest(classes = LogsApp.class)
public class LoggssResourceIT {

    private static final Integer DEFAULT_ID_VENTA = 1;
    private static final Integer UPDATED_ID_VENTA = 2;

    private static final String DEFAULT_PASO = "AAAAAAAAAA";
    private static final String UPDATED_PASO = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTADO = "AAAAAAAAAA";
    private static final String UPDATED_RESULTADO = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLICACION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLICACION = "BBBBBBBBBB";

    @Autowired
    private LoggssRepository loggssRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLoggssMockMvc;

    private Loggss loggss;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoggssResource loggssResource = new LoggssResource(loggssRepository);
        this.restLoggssMockMvc = MockMvcBuilders.standaloneSetup(loggssResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loggss createEntity(EntityManager em) {
        Loggss loggss = new Loggss()
            .idVenta(DEFAULT_ID_VENTA)
            .paso(DEFAULT_PASO)
            .resultado(DEFAULT_RESULTADO)
            .explicacion(DEFAULT_EXPLICACION);
        return loggss;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loggss createUpdatedEntity(EntityManager em) {
        Loggss loggss = new Loggss()
            .idVenta(UPDATED_ID_VENTA)
            .paso(UPDATED_PASO)
            .resultado(UPDATED_RESULTADO)
            .explicacion(UPDATED_EXPLICACION);
        return loggss;
    }

    @BeforeEach
    public void initTest() {
        loggss = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoggss() throws Exception {
        int databaseSizeBeforeCreate = loggssRepository.findAll().size();

        // Create the Loggss
        restLoggssMockMvc.perform(post("/api/loggsses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loggss)))
            .andExpect(status().isCreated());

        // Validate the Loggss in the database
        List<Loggss> loggssList = loggssRepository.findAll();
        assertThat(loggssList).hasSize(databaseSizeBeforeCreate + 1);
        Loggss testLoggss = loggssList.get(loggssList.size() - 1);
        assertThat(testLoggss.getIdVenta()).isEqualTo(DEFAULT_ID_VENTA);
        assertThat(testLoggss.getPaso()).isEqualTo(DEFAULT_PASO);
        assertThat(testLoggss.getResultado()).isEqualTo(DEFAULT_RESULTADO);
        assertThat(testLoggss.getExplicacion()).isEqualTo(DEFAULT_EXPLICACION);
    }

    @Test
    @Transactional
    public void createLoggssWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loggssRepository.findAll().size();

        // Create the Loggss with an existing ID
        loggss.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoggssMockMvc.perform(post("/api/loggsses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loggss)))
            .andExpect(status().isBadRequest());

        // Validate the Loggss in the database
        List<Loggss> loggssList = loggssRepository.findAll();
        assertThat(loggssList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLoggsses() throws Exception {
        // Initialize the database
        loggssRepository.saveAndFlush(loggss);

        // Get all the loggssList
        restLoggssMockMvc.perform(get("/api/loggsses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loggss.getId().intValue())))
            .andExpect(jsonPath("$.[*].idVenta").value(hasItem(DEFAULT_ID_VENTA)))
            .andExpect(jsonPath("$.[*].paso").value(hasItem(DEFAULT_PASO)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].explicacion").value(hasItem(DEFAULT_EXPLICACION)));
    }
    
    @Test
    @Transactional
    public void getLoggss() throws Exception {
        // Initialize the database
        loggssRepository.saveAndFlush(loggss);

        // Get the loggss
        restLoggssMockMvc.perform(get("/api/loggsses/{id}", loggss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loggss.getId().intValue()))
            .andExpect(jsonPath("$.idVenta").value(DEFAULT_ID_VENTA))
            .andExpect(jsonPath("$.paso").value(DEFAULT_PASO))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO))
            .andExpect(jsonPath("$.explicacion").value(DEFAULT_EXPLICACION));
    }

    @Test
    @Transactional
    public void getNonExistingLoggss() throws Exception {
        // Get the loggss
        restLoggssMockMvc.perform(get("/api/loggsses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoggss() throws Exception {
        // Initialize the database
        loggssRepository.saveAndFlush(loggss);

        int databaseSizeBeforeUpdate = loggssRepository.findAll().size();

        // Update the loggss
        Loggss updatedLoggss = loggssRepository.findById(loggss.getId()).get();
        // Disconnect from session so that the updates on updatedLoggss are not directly saved in db
        em.detach(updatedLoggss);
        updatedLoggss
            .idVenta(UPDATED_ID_VENTA)
            .paso(UPDATED_PASO)
            .resultado(UPDATED_RESULTADO)
            .explicacion(UPDATED_EXPLICACION);

        restLoggssMockMvc.perform(put("/api/loggsses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoggss)))
            .andExpect(status().isOk());

        // Validate the Loggss in the database
        List<Loggss> loggssList = loggssRepository.findAll();
        assertThat(loggssList).hasSize(databaseSizeBeforeUpdate);
        Loggss testLoggss = loggssList.get(loggssList.size() - 1);
        assertThat(testLoggss.getIdVenta()).isEqualTo(UPDATED_ID_VENTA);
        assertThat(testLoggss.getPaso()).isEqualTo(UPDATED_PASO);
        assertThat(testLoggss.getResultado()).isEqualTo(UPDATED_RESULTADO);
        assertThat(testLoggss.getExplicacion()).isEqualTo(UPDATED_EXPLICACION);
    }

    @Test
    @Transactional
    public void updateNonExistingLoggss() throws Exception {
        int databaseSizeBeforeUpdate = loggssRepository.findAll().size();

        // Create the Loggss

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoggssMockMvc.perform(put("/api/loggsses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loggss)))
            .andExpect(status().isBadRequest());

        // Validate the Loggss in the database
        List<Loggss> loggssList = loggssRepository.findAll();
        assertThat(loggssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLoggss() throws Exception {
        // Initialize the database
        loggssRepository.saveAndFlush(loggss);

        int databaseSizeBeforeDelete = loggssRepository.findAll().size();

        // Delete the loggss
        restLoggssMockMvc.perform(delete("/api/loggsses/{id}", loggss.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loggss> loggssList = loggssRepository.findAll();
        assertThat(loggssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
