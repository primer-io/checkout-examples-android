# Server Deno Hono

In this example we build a server to integrate with Primer's API.

It uses [Deno](https://deno.land) as the runtime and [Hono](https://hono.dev) as the HTTP server framework, but feel free to build your server however you'd like.

## Running on your browser

[Click here to immediately launch it on your browser](https://stackblitz.com/github/primer-io/checkout-web/tree/main/examples/server-deno-hono).

Once it's open, make sure to:

1. Create a new file `.env` and copy contents from `.env.example` into it
2. Get an `API_KEY` from your Primer Dashboard and paste it there
   - Example: `API_KEY=1234-foo-bar-4321`

## Running locally

1. Install [Deno](https://deno.land)
2. Follow the same instructions described on the section above about the `API_KEY`
3. Execute the following script on a terminal window:
   ```sh
   deno task start
   ```
