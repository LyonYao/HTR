package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.Person;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.PersonRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public Person savePerson(Person person) {
        try {

            SystemLog log = new SystemLog(Constants.MODULE_PERSON,person.getName());
            if(StringUtils.isNotBlank(person.getUuid())){
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            person = personRepository.save(person);
            log.setRecordId(person.getUuid());
            systemLogRepository.save(log);
            return person;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update Person" + person.getName() + " fail!");
        }
        return null;
    }

    @Override
    public boolean removePersons(List<Person> personList) {
        try {
            SystemLog log;
            for(Person person : personList){
                log = new SystemLog(Constants.MODULE_PERSON, person.getName(), person.getUuid(), Constants.OPERATYPE_DELETE);
                person.setActive(false);
                personRepository.save(person);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete Person fail!");
        }
        return false;
    }

    @Override
    public Page<Person> findAll(Map<String,Object> filterParams, Pageable pageable) {
        Map<String,SearchFilter> filterMap=SearchFilter.parse(filterParams);
        return personRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), Person.class), pageable);
    }
}
