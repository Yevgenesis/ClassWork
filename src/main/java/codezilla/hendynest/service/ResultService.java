package codezilla.hendynest.service;

import codezilla.hendynest.model.ResponseDto;
import codezilla.hendynest.model.ResultDto;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Value("${security.token}")
    private String token;

    // Метод для получения ответа
    public ResponseDto getResponse(MultiValueMap<String, String> map, HttpServletRequest request) {

        List<ResultDto> result = new ArrayList<>();

        if(!isValidToken(request)){
            result.add(new ResultDto("Error token validation"));
            return new ResponseDto("404", result);
        }


        // Сортировка параметров и формирование строки запроса
        String sortedString = map.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .sorted(Map.Entry.comparingByKey())
                .flatMap(e -> e.getValue().stream().map(s -> Map.entry(e.getKey(), s)))
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));

        // Получение хэша SHA-256 от строки запроса
        String hash = sha256(sortedString);

        // Формирование списка результатов

        result.add(new ResultDto(hash));


        // Возвращение объекта ResponseDto с успешным статусом и результатом
        return new ResponseDto("success", result);
    }

    // Метод для вычисления хэша SHA-256
    private String sha256(String input) {
        return DigestUtils.sha256Hex(input);
    }

    // Метод для кодирования строки
    private String encode(String original) {
        return URLEncoder.encode(original, Charset.defaultCharset());
    }

    private boolean isValidToken(HttpServletRequest request) {
        String header = request.getHeader("token");
        return !(header == null || !header.equals(token));
    }
}
