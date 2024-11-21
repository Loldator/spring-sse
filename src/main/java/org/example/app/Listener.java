package org.example.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class Listener {
	private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

	private final Deque<SseEmitter> emitters = new ArrayDeque<>(100);

	public Listener() {}

	@EventListener
	public void onFooEvent(FooEvent event) {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(event);
			} catch (Throwable _) {
				LOG.debug("Failed to send event to emitter: {}", emitter.hashCode());
			}
		}
	}

	public void addEmitter(SseEmitter emitter) {
		this.emitters.add(emitter);
//		emitter.onError(e -> dropEmitter(emitter));
		emitter.onCompletion(() -> dropEmitter(emitter));
		emitter.onTimeout(() -> dropEmitter(emitter));
	}


	public void dropEmitter(SseEmitter emitter) {
		this.emitters.remove(emitter);
	}

}
