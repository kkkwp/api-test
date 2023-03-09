package com.kakao.apitest.youtube.service;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.kakao.apitest.youtube.dto.YouTubeDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class YouTubeImpl implements YouTubeService {

	@Value("${youtube-apiKey}")
	private String API_KEY;
	@Value("${youtube-fields}")
	private String SEARCH_FIELDS;

	private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
	private static final String SEARCH_TYPE = "video";
	private static final String API_APPLICATION = "google-youtube-api-search";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
	private static YouTube youTube;

	// youtube data api 요청 생성
	static {
		youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {})
				.setApplicationName(API_APPLICATION)
				.build();
	}

	// 내가 원하는 정보 지정
	@Override
	public List<YouTubeDto> youtubeSearch(String searchQuery, int maxSearch) {

		log.info("유튜브 탐색 시작... " + searchQuery);

		List<YouTubeDto> rvalue = new ArrayList<YouTubeDto>();

		try {
			if (youTube != null) {
				// 검색 결과를 위한 api request 정의
				YouTube.Search.List search = youTube.search().list("id,snippet");
				search.setKey(API_KEY);
				search.setQ(searchQuery);
				search.setType(SEARCH_TYPE); // 검색 결과로 video 형태만 가져오도록 함
				search.setFields(SEARCH_FIELDS); // api에서 정해놓은 field만 이용하여 검색함으로써 검색 효율을 높임
				// 최대 검색 개수 설정
				if (maxSearch < 1) {
					search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
				} else {
					search.setMaxResults((long) maxSearch);
				}

				// api를 호출하고 결과 출력
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();

				if (searchResultList != null && searchResultList.size() > 0) {
					for (SearchResult r : searchResultList) {
						YouTubeDto item = new YouTubeDto(
								YOUTUBE_URL + r.getId().getVideoId(),
								r.getSnippet().getTitle(),
								r.getSnippet().getThumbnails().getDefault().getUrl(),
								r.getSnippet().getDescription());
						rvalue.add(item);
					}
				} else {
					log.info("youtube api로부터 받은 검색 결과 없음");
				}
			} else {
				log.warn("youtube api가 올바르게 initialized 되지 않음");
			}

		} catch (GoogleJsonResponseException e) {
			log.warn("서비스 에러: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			log.warn("IO 에러: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			log.warn("서버 에러", t);
			t.printStackTrace();
		}
		return rvalue;
	}

}
