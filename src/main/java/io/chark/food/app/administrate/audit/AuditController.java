package io.chark.food.app.administrate.audit;

import com.fasterxml.jackson.annotation.JsonView;
import io.chark.food.domain.audit.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String audit() {
        return "administrate/audit";
    }

    /**
     * Get list of audit messages.
     *
     * @param id if specified, specific users audit messages are returned.
     * @return list of audit messages.
     */
    @ResponseBody
    @JsonView(AuditMessage.MinimalView.class)
    @RequestMapping(value = "/api/audit", method = RequestMethod.GET)
    public List<AuditMessage> getAuditMessages(@RequestParam(required = false) Long id) {
        if (id == null) {
            return auditService.getAuditMessages();
        }
        return auditService.getAuditMessages(id);
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