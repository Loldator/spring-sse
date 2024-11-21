package org.example.app;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FooController {
	private final Listener listener;
	private final Publisher publisher;
	public FooController(Listener listener, Publisher publisher) {
		this.listener = listener;
		this.publisher = publisher;
	}

	@ExceptionHandler({IOException.class})
	public void doNothing(HandlerMethod method, Exception e) throws Exception {
		if (method.getMethod().getName().equals("stream")) {
			// ignore closed sse-connections
			return;
		}
		throw e;
	}

	@PostMapping("/publish")
	public void publish() {
			publisher.publishFoo(new FooEvent(UUID.randomUUID().toString()));
	}

	@GetMapping("/stream")
	public SseEmitter stream() throws IOException {
		final var emitter = new SseEmitter(99999999L);
		listener.addEmitter(emitter);
		return emitter;
	}
}
