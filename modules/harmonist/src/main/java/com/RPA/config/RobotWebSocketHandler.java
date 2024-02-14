package com.RPA.config;

//import com.RPA.entity.InMemoryRobot;
//import com.RPA.service.RobotService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.net.http.WebSocket;
//import java.util.regex.Pattern;
//
//@Component
//@RequiredArgsConstructor
//public class RobotWebSocketHandler extends WebSocketHandler {
//
//    /**
//     * Выполняется когда пытается открыться новое соединение
//     * @param request
//     * @param protocol протокол (бывает двух видов ws и wss)
//     * @return
//     */
//    @Override
//    public WebSocket doWebSocketConnect(HttpServletRequest request,
//                                        String protocol) {
//
//        // У нас есть два варианта
//        // Либо мы не пускаем клиента и вызываем исключение
//        //    throw new Exception();
//        // Либо возвращаем объект, который будет соединять сервер с клиентом
//        //   и обрабатывать запросы от клиента
//        return new ChatWebSocket();
//    }
//
//    private class ChatWebSocket extends TextWebSocketHandler {
//
//        /**
//         * Хранилище соединения
//         */
//        private Connection connection;
//
//        private final Pattern authCmdPattern = Pattern.compile("^\\/auth ([\\S]+).*");
//        private final Pattern getUsersCmdPattern = Pattern.compile("^\\/getUsers.*");
//        private final Pattern helpCmdPattern = Pattern.compile("^\\/help.*");
//
//        @Override
//        public void onOpen(Connection connection) {
//
//            // Сохраняем соединение в свойство ChatWebSocket::connection
//            this.connection = connection;
//        }
//
//        /**
//         * Выполняется когда пришло новое сообщение
//         * @param data
//         */
//        @Override
//        public void onMessage(String data) {
//
//            // На всякий случай удаляем теговые дескрипторы
//            data = data.replaceAll("<", "<").replaceAll(">", ">");
//
//            // Если пришла команда авторизации
//            if (authCmdPattern.matcher(data).matches()) {
//                Matcher matcher = authCmdPattern.matcher(data);
//                matcher.find();
//
//                // Устанавливаем новый ник пользователю
//                userName = matcher.group(1);
//
//                try {
//                    // Цикл шарит по набору сокетов ChatWebSocketHandler::webSockets
//                    for (ChatWebSocket webSocket : webSockets) {
//
//                        // и отправляет сообщение, что подключился новый пользователь
//                        webSocket.connection.sendMessage("inf|"
//                                + (webSocket.equals(this)
//                                ? "Вы успешно авторизировались"
//                                : ("В чат подключился <b>" + userName + "</b>")));
//                    }
//                } catch (IOException x) {
//
//                    // Все ошибки будут приводить к разъединению клиента от сервера
//                    connection.disconnect();
//                }
//
//                // Если пришла команда получения списка пользователей
//            } else if (getUsersCmdPattern.matcher(data).matches()) {
//
//                String userList = "";
//
//                // Цикл шарит по набору сокетов ChatWebSocketHandler::webSockets
//                for (ChatWebSocket webSocket : webSockets) {
//                    userList += webSocket.userName + ", ";
//                }
//                userList = userList.substring(0, userList.length() - 2);
//
//                try {
//
//                    // Отсылаем список активных пользователей
//                    connection.sendMessage("inf|Список активных пользователей: " + userList);
//
//                } catch (IOException x) {
//
//                    // Все ошибки будут приводить к разъединению клиента от сервера
//                    connection.disconnect();
//                }
//
//                // Если пришла команда получения помощи
//            } else if (helpCmdPattern.matcher(data).matches()) {
//
//                String helpMessage = "Отправлять сообщения можно просто написав "
//                        + "их в поле для ввода и нажать Enter.<br />"
//                        + "Чат поддерживает три команды:<br />"
//                        + "<ul><li><b>/help</b> - для распечатки этого сообщения</li>"
//                        + "<li><b>/getUsers</b> - для получения списка пользователей</li>"
//                        + "<li><b>/auth <i>ник</i></b> - для авторизации</li></ul>";
//
//                try {
//                    // Отсылаем инструкцию
//                    connection.sendMessage("inf|" + helpMessage);
//
//                } catch (IOException x) {
//
//                    // Все ошибки будут приводить к разъединению клиента от сервера
//                    connection.disconnect();
//                }
//
//                // Если пришла не команда а сообщение
//            } else {
//
//
//                try {
//
//                    // Если пользователь не авторизирован
//                    if (userName == null) {
//                        connection.sendMessage("err|Вы не авторизированны<br />"
//                                + "Используйте команду <b>/help</b> для помощи");
//                        return;
//                    }
//                    // Цикл шарит по набору сокетов ChatWebSocketHandler::webSockets
//                    for (ChatWebSocket webSocket : webSockets) {
//
//                        // и каждому рассылает сообщение с флагом in для всех
//                        // кроме автора, автору - флаг out
//                        webSocket.connection.sendMessage((webSocket.equals(this) ? "out|" : ("in|" + userName + "|")) + data);
//                    }
//                } catch (IOException x) {
//
//                    // Все ошибки будут приводить к разъединению клиента от сервера
//                    connection.disconnect();
//                }
//            }
//
//        }
//
//        /**
//         * Выполняется когда клиент разъединяется от сервера
//         * @param closeCode
//         * @param message
//         */
//        @Override
//        public void onClose(int closeCode, String message) {
//
//        }
//    }
//}