package codezilla.hendynest.service;

import codezilla.hendynest.model.ResponseDto;
import codezilla.hendynest.model.ResultDto;
import org.apache.commons.codec.digest.DigestUtils;
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

    // Метод для получения ответа
    public ResponseDto getResponse(MultiValueMap<String, String> map) {
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
        List<ResultDto> result = new ArrayList<>();
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
}
