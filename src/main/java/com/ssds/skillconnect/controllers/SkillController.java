package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Skill;
import com.ssds.skillconnect.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/skill")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/all")
    List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping("/search/{skillName}")
    List<Skill> findSimilarSkills(@PathVariable String skillName) {
        return skillService.findSimilarSkills(skillName);
    }


}
