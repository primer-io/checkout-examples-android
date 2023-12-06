export async function post<T>(
  url: string,
  body: Record<string, unknown>,
  headers?: HeadersInit,
) {
  const response = await fetch(url, {
    ...(body && { body: JSON.stringify(body) }),
    ...(headers && { headers }),
    method: 'POST',
  });

  const json = (await response.json()) as T & { error?: ServerError };

  if (json.error)
    throw new Error(json.error.description, { cause: json.error });

  return json as T;
}

type ServerError = {
  errorId: string;
  description: string;
  diagnosticsId: string;
  validationErrors: { model: string; errors: string[] }[];
};
