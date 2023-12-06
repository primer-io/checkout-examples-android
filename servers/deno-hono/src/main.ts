import { Hono } from 'hono/mod.ts';
import 'std/dotenv/load.ts';
import { createClientSession } from './api/createClientSession.ts';

const app = new Hono();

app.get('/', (c) =>
  c.text(['Available endpoints:', '', '  POST /client-session'].join('\n')),
);

app.post('/client-session', async (c) => {
  const body = await c.req.json()

  console.log(body);

  const { amount, currencyCode } = body as {
    amount: number;
    currencyCode: string;
  };

  const res = await createClientSession({
    amount,
    currencyCode,
  });

  return c.json(res);
});

await Deno.serve(app.fetch);
