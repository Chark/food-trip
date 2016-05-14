package io.chark.food.app.administrate.audit;

import io.chark.food.domain.audit.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/administrate")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Get audit message page.
     *
     * @return audit message template.
     */
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public String audit(Model model) {
        model.addAttribute("messages", auditService.getAuditMessages());
        return "administrate/audit";
    }

    /**
     * Get a single audit message for more info.
     *
     * @param id audit message id.
     * @return audit message json.
     */
    @ResponseBody
    @RequestMapping(value = "/api/audit/{id}", method = RequestMethod.GET)
    public AuditMessage getAuditMessage(@PathVariable long id) {
        return auditService.getAuditMessage(id);
    }
}