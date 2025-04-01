package ru.markov.application.service;

import com.vaadin.flow.component.upload.UploadI18N;
import java.util.Arrays;

public class UploadRussianI18N extends UploadI18N {
    public UploadRussianI18N(String target) {
        setDropFiles(new DropFiles().setOne("Перетащите сюда файл")
                .setMany("Перетащите сюда файлы"));

        switch (target) {
            case "backup" -> setAddFiles(new AddFiles().setOne("Выберите файл бэкапа .bin")
                    .setMany("Выберите файл бэкапа"));
            case "plan" -> setAddFiles(new AddFiles().setOne("Выберите файл с текущим графиком .png")
                    .setMany("Выберите файл с текущим планом .png"));
            case "otpusk" -> setAddFiles(new AddFiles().setOne("Выберите файл с текущим планом отпусков .png")
                    .setMany("Выберите файл с текущим планом отпусков .png"));
        }

        setError(new Error().setTooManyFiles("Слишком много файлов")
                .setFileIsTooBig("Файл много весит")
                .setIncorrectFileType("Неправильный формат файла"));
        setUploading(new Uploading().setStatus(new Uploading.Status()
                        .setConnecting("Соединяюсь...").setStalled("Остановка операции")
                        .setProcessing("Обработка файла...").setHeld("Ждёт очереди"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("времени осталось: ")
                        .setUnknown("невозможно вычислить оставшееся время"))
                .setError(new Uploading.Error()
                        .setServerUnavailable("Сервер не отвечает")
                        .setUnexpectedServerError("Ошибка сервера")
                        .setForbidden("Запрещено")));
        setUnits(new Units().setSize(Arrays.asList("Байт", "КБ", "МБ", "ГБ", "ТБ",
                "ПБ", "ЁБ", "ЗБ", "УБ")));
    }
}