package com.example.jmix.service;

import com.airbyte.api.Airbyte;
import com.airbyte.api.models.operations.ListSourcesRequest;
import com.airbyte.api.models.operations.ListSourcesResponse;
import com.airbyte.api.models.shared.SourceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AirbyteService {

    private static final Logger log = LoggerFactory.getLogger(AirbyteService.class);

    private final Airbyte airbyte;

    public AirbyteService(Airbyte airbyte) {
        this.airbyte = airbyte;
    }

    public List<SourceResponse> getSources() {
        try {
            ListSourcesRequest req = ListSourcesRequest.builder()
                    .workspaceIds(List.of("b0b5161d-3434-4816-a25a-eed749785809"))
                    .build();

            ListSourcesResponse res = airbyte.sources()
                    .listSources()
                    .request(req)
                    .call();

            if (res.sourcesResponse().isPresent()) {
                return res.sourcesResponse().get().data();
            }
            log.warn("Airbyte returned empty SourcesResponse");
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("Failed to fetch sources from Airbyte", e);
            return Collections.emptyList();
        }
    }
}
