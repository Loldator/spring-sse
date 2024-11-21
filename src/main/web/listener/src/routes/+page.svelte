<script lang="ts">
	import {onMount} from "svelte";

	const events: MessageEvent[] = $state([]);
	let isOpen = $state(false);
	let isError = $state(false);

	onMount(async () => {
		let es = new EventSource('http://localhost:8080/stream', {withCredentials: false});
		es.onopen = (o) => {
			isOpen = true;
		};
		es.onerror = (o) => {
			isError = true;
		};
		es.onmessage = (e) => {
			console.log(e);
			events.unshift(e);
		};
	})


</script>

{#if isOpen}
	<p>OPEN</p>
{/if}

{#if isError}
	<p>ERROR</p>
{/if}

<ul class="flex flex-col space-y-2 p-2">
	{#each events as e}
		<li class="bg-gray-200 odd:bg-blue-300">{e.data}</li>
	{/each}
</ul>
