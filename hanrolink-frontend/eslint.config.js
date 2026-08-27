import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'
import eslintConfigPrettier from 'eslint-config-prettier'
import { defineConfig, globalIgnores } from 'eslint/config'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      js.configs.recommended,
      ...tseslint.configs.recommended,
      reactHooks.configs.flat.recommended,
      reactRefresh.configs.vite,
      eslintConfigPrettier,
    ],
    languageOptions: {
      globals: globals.browser,
    },
    rules: {
      'no-eval': 'error',
      'no-implied-eval': 'error',
      'no-new-func': 'error',
      'no-script-url': 'error',

      'no-restricted-properties': [
        'error',
        {
          property: 'innerHTML',
          message: 'XSSの原因になるためinnerHTMLは使用しないでください。',
        },
        {
          property: 'outerHTML',
          message: 'XSSの原因になるためouterHTMLは使用しないでください。',
        },
        {
          property: 'insertAdjacentHTML',
          message:
            'XSSの原因になるためinsertAdjacentHTMLは使用しないでください。',
        },
        {
          object: 'document',
          property: 'write',
          message: 'XSSの原因になるためdocument.writeは使用しないでください。',
        },
      ],

      'no-restricted-syntax': [
        'error',
        {
          selector: "JSXAttribute[name.name='dangerouslySetInnerHTML']",
          message:
            'XSSの原因になるためdangerouslySetInnerHTMLは使用しないでください。',
        },
        {
          selector: "JSXAttribute[name.name='srcDoc']",
          message: 'XSSの原因になるためsrcDocは使用しないでください。',
        },
      ],
    },
  },
])
