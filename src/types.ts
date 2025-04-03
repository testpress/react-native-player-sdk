export type TpStreamsPlayerProps = {
  videoId: string;
  accessToken: string;
  enableDownload?: boolean;
  autoPlay?: boolean;
  startAt?: number;
  offlineLicenseExpireTime?: number;
  downloadMetadata?: Record<string, string>;
  style?: import('react-native').ViewStyle;
};
