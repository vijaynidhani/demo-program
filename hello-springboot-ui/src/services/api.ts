import axios, { AxiosInstance } from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081'

const createClient = (username: string, password: string): AxiosInstance => {
  const basicAuth = Buffer.from(`${username}:${password}`).toString('base64')
  return axios.create({
    baseURL: API_URL,
    headers: {
      'Authorization': `Basic ${basicAuth}`,
      'Content-Type': 'application/json'
    }
  })
}

export const apiClient = {
  getHello: async () => {
    const response = await axios.get(`${API_URL}/hello`)
    return response.data
  },

  createPayment: async (
    amount: number,
    toAccount: string,
    fromAccount: string,
    name: string,
    method: string,
    username: string,
    password: string,
    idempotencyKey?: string
  ) => {
    const client = createClient(username, password)
    const response = await client.post('/payments', {
      amount,
      toAccount,
      fromAccount,
      name,
      method
    }, {
      headers: {
        ...(idempotencyKey && { 'Idempotency-Key': idempotencyKey })
      }
    })
    return response.data
  },

  getPaymentsReport: async (username: string, password: string) => {
    const client = createClient(username, password)
    const response = await client.get('/payments/report')
    return response.data
  }
}
